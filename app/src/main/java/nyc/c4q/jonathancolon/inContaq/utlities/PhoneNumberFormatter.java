package nyc.c4q.jonathancolon.inContaq.utlities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber.PhoneNumber;


/**
 * Created by jonathancolon on 6/9/17.
 */

public class PhoneNumberFormatter {

    private Context context;
    private PhoneNumberUtil instance;

    public PhoneNumberFormatter(Context context, PhoneNumberUtil instance) {
        this.context = context;
        this.instance = instance;
    }

    @NonNull
    private PhoneNumberUtil getPhoneNumberUtil() {
        return PhoneNumberUtil.createInstance(context);
    }

    public String formatPhoneNumber(String phoneNumber) throws NumberParseException {
        removeSpecialCharacters(phoneNumber);
        phoneNumber = removeCountryCode(phoneNumber);
        return phoneNumber;
    }

    private String removeSpecialCharacters(String phoneNumber) {
        return phoneNumber.replaceAll("[()\\s-]+", "");
    }

    @NonNull
    private String removeCountryCode(String phoneNumber) throws NumberParseException {
        Log.e("CONVERTER", "ORIGINAL: " + phoneNumber);
        PhoneNumber number = instance.parse(phoneNumber, "US");
        String formattedNumber = instance.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
        Log.e("CONVERTER", "FORMATTED: " + formattedNumber);

        Log.e("CONVERTER", "PERFECT: " + PhoneNumberUtil.normalizeDigitsOnly(formattedNumber));
        return PhoneNumberUtil.normalizeDigitsOnly(formattedNumber);
    }
}
