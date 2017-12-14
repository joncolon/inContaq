package nyc.c4q.jonathancolon.inContaq.utlities;

import android.support.annotation.NonNull;
import android.util.Log;

import javax.inject.Inject;

import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber.PhoneNumber;


public class PhoneNumberFormatter {

    private PhoneNumberUtil instance;

    @Inject
    public PhoneNumberFormatter(PhoneNumberUtil instance) {
        this.instance = instance;
    }

    public String formatPhoneNumber(String phoneNumber) throws NumberParseException {
        phoneNumber = removeSpecialCharacters(phoneNumber);
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
