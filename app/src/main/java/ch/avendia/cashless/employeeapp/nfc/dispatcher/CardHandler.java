package ch.avendia.cashless.employeeapp.nfc.dispatcher;

import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.util.Log;

import com.google.common.primitives.Ints;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import ch.avendia.cashless.employeeapp.nfc.CashlessCard;
import ch.avendia.cashless.employeeapp.nfc.CashlessCardType;

/**
 * Created by Markus on 16.09.2015.
 */
public class CardHandler {

    public static final int MIFARE_READ_PAGES = 4;
    private MifareUltralight mifareUltralight;

    private final int START_UID = 6;
    private final int END_UID = 14;
    private final int START_CASH = 15;
    private final int START_ACCESS = 16;
    private final int START_USERID = 17;
    private final int END_USERID = 25;
    private final int START_EVENTID = 26;
    private final int START_DATA = 6;
    private final int END_DATA = 30;
    private String cardId;

    public CardHandler(Tag tag) throws IOException {
        mifareUltralight = MifareUltralight.get(tag);
        if(MifareUltralight.TYPE_ULTRALIGHT_C != mifareUltralight.getType()) {
            throw new IOException("Wrong Type");
        }
        mifareUltralight.connect();
        cardId = new String(tag.getId());
        mifareUltralight.setTimeout(500);
    }

    public void writeClientTicket(String uid, int cash, int access) throws IOException {

        byte[] uidBytes = uid.getBytes();
        byte[] cashBytes = Ints.toByteArray(cash);
        byte[] accessBytes = Ints.toByteArray(access);

        writeUID(uidBytes);
        writeCash(cashBytes);
        writeAccess(accessBytes);

        mifareUltralight.writePage(5, new byte[]{0x00, 0x00, 0x00, 0x00});
    }


    public void writeEmployeeCard(String uid, int cash, int access, String userId, int eventId) throws IOException {
        writeClientTicket(uid, cash, access);

        byte[] userIdBytes = userId.getBytes();
        byte[] eventIdBytes = Ints.toByteArray(eventId);

        writeUserId(userIdBytes);
        writeEventId(eventIdBytes);

        mifareUltralight.writePage(5, new byte[]{0x00, 0x00, 0x00, 0x01});
    }

    public int getCash() {
        return readIntPage(START_CASH);
    }

    public CashlessCard readCard() throws IOException {
        byte[] info = readSinglePage(5);

        CashlessCard cashlessCard = new CashlessCard();

        String uid = readPages(START_UID, END_UID);
        int cash = readIntPage(START_CASH);
        int access= readIntPage(START_ACCESS);

        cashlessCard.setUid(uid);
        cashlessCard.setCash(cash);
        cashlessCard.setAccess(access);
        cashlessCard.setCashlessCardType(CashlessCardType.CLIENT);

        if(info[3] == 1) {
            String userId = readPages(START_USERID, END_USERID);
            int eventId = readIntPage(START_EVENTID);

            cashlessCard.setUserId(userId);
            cashlessCard.setEventId(eventId);

            cashlessCard.setCashlessCardType(CashlessCardType.EMPLOYEE);
        }

        return cashlessCard;
    }


    private int readIntPage(int page) {

        byte[] data = new byte[0];
        try {
            data = readSinglePage(page);

            ByteBuffer bb = ByteBuffer.wrap(data);
            if(false) {
                bb.order(ByteOrder.LITTLE_ENDIAN);
            }

            return bb.getInt();
        } catch (IOException e) {
            return 0;
        }
    }

    private byte[] readSinglePage(int page) throws IOException {
        byte[] data = mifareUltralight.readPages(page);

        return Arrays.copyOf(data, MifareUltralight.PAGE_SIZE);
    }

    private String readPages(int first, int last) {
        StringBuilder sb = new StringBuilder();

        int index = first;
        try {
            while(index <= last) {
                byte[] data = mifareUltralight.readPages(index);

                Log.d("NFC Read", "Pages:" + index + ", Data:" + new String(data));
                Log.d("NFC Read", "Length: " + data.length);
                for(int j=0;j<MIFARE_READ_PAGES;j++) {
                    if(index+j<=last) {
                        byte[] pageData = Arrays.copyOfRange(data, 0+j*MifareUltralight.PAGE_SIZE, MifareUltralight.PAGE_SIZE+j*MifareUltralight.PAGE_SIZE);
                        String dataString = new String(pageData);
                        sb.append(dataString);
                        Log.d("NFC Read", "Page:" + (first+j) + ", Data:" + dataString);

                    }
                }


                index+= MIFARE_READ_PAGES;
            }
        } catch (IOException e) {
            return null;
        }

        return sb.toString();
    }



    private void writeUID(byte[] uid) throws IOException {

        writeByte(uid, START_UID, END_UID);
    }

    public void writeCash(int cash) throws IOException {
        writeCash(Ints.toByteArray(cash));
    }

    private void writeCash(byte[] cash) throws IOException {
        Log.d("NFC", "write to page " + START_CASH + ": " + cash + ", length: " + cash.length);
        mifareUltralight.writePage(START_CASH, cash);
    }


    private void writeAccess(byte[] access) throws IOException {
        Log.d("NFC", "write to page " + START_ACCESS + ": " + access + ", length: " + access.length);
        mifareUltralight.writePage(START_ACCESS, access);
    }

    private void writeUserId(byte[] userIdBytes) throws IOException {

        writeByte(userIdBytes, START_USERID, END_USERID);
    }

    private void writeEventId(byte[] eventIdBytes) throws IOException {
        Log.d("NFC", "write to page " + START_EVENTID + ": " + eventIdBytes + ", length: " + eventIdBytes.length);
        mifareUltralight.writePage(START_EVENTID, eventIdBytes);
    }

    private void writeByte(byte[] data, int first, int last) throws IOException {
        int pages = last - first + 1;  //+1 because first page is also writeable

        if(data.length / MifareUltralight.PAGE_SIZE > pages) {
            throw new IOException("Not enough pages available, first: " + first + ", last: " +last +", length: " + data.length);
        }

        for(int i = 0;i<pages;i++) {
            byte[] pageData = Arrays.copyOfRange(data, i*MifareUltralight.PAGE_SIZE, i*MifareUltralight.PAGE_SIZE+MifareUltralight.PAGE_SIZE);
            Log.d("NFC", "write to page " + (first + i) + ": " + pageData + ", length: " + pageData.length);
            mifareUltralight.writePage(first+i, pageData);
        }
    }


    public void reset() throws IOException {

            for(int i=START_DATA;i<=END_DATA;i++) {
                Log.d("Tag reset", "empty page: " + i);
                mifareUltralight.writePage(i, new byte[]{0x00, 0x00, 0x00, 0x00});
            }

        Log.i("Tag reset", "success");
    }


    public String getCardId() {
        return cardId;
    }
}
