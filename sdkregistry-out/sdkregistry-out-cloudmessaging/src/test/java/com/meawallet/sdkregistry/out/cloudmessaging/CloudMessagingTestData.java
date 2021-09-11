package com.meawallet.sdkregistry.out.cloudmessaging;

import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedEncMacKeys;
import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedPushKeys;
import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedRgk;
import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedSdkKeyset;
import com.meawallet.sdkregistry.api.dto.cryptography.EncryptedSecretKey;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainEncMacKeys;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainPushKeys;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainRgk;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainSdkKeyset;
import com.meawallet.sdkregistry.api.dto.cryptography.PlainSecretKey;
import com.meawallet.sdkregistry.api.dto.location.GpsLocation;
import com.meawallet.sdkregistry.api.dto.sdk.Sdk;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;

import java.math.BigDecimal;

public class CloudMessagingTestData {

    // encrypted keys
    public static final String ENC_PUSH_DATA_ENCRYPTION_KEY_UNDER_RGK = "7ff868751fee5e2f9eab00c83eac3304565dc904a7e892602ef84268cc8335e0";
    public static final String ENC_PUSH_DATA_MAC_KEY_UNDER_RGK = "ffbf022ffa1fc12ca586dea475a1ca6a565dc904a7e892602ef84268cc8335e0";
    public static final String ENC_PUSH_MESSAGE_MAC_KEY_UNDER_RGK = "d67424fb7c96096f2c3992000212b1b6565dc904a7e892602ef84268cc8335e0";
    public static final String ENC_ATTESTATION_ENCRYPTION_KEY_UNDER_RGK = "e8a4a8cf61c9bc80852af34dfae4339a565dc904a7e892602ef84268cc8335e0";
    public static final String ENC_ATTESTATION_MAC_KEY_UNDER_RGK = "c35dde5a2709951624a5027118b4f73f565dc904a7e892602ef84268cc8335e0";
    public static final String ENC_TRANSACTION_ENCRYPTION_KEY_UNDER_RGK = "2b43d3b6a66dbdf04d765813526b1e91565dc904a7e892602ef84268cc8335e0";
    public static final String ENC_TRANSACTION_MAC_KEY_UNDER_RGK = "e2ebd0fdbd382c1978d8c19007b7f611565dc904a7e892602ef84268cc8335e0";
    public static final String ENC_SECRET_KEY_UNDER_RGK = "202bdfd06175558302538c97c5bb1f9a565dc904a7e892602ef84268cc8335e0";

    public static final String ENC_RGK_ENC_KEY_UNDER_SHARED = "ec01b2577c5b29aba408aad84316f67150bfb943789b3a5ffc05e3bc99e54dbc";
    public static final String ENC_RGK_MAC_KEY_UNDER_SHARED = "a8fd1ca3a3ac67ca92c4836752eaa93650bfb943789b3a5ffc05e3bc99e54dbc";

    // plain keys
    public static final String SHARED_KEY = "cb98cf95ca290708ab1ae47e265c0f48";
    public static final String SHARED_KEY_FINGERPRINT = "24378cdbdbdc1e226a0ce89285c30431950418174a72a7fb990bd20f1b13825a";
    public static final String KEYSET_ID = "eaa08cd1c42059975f42f20332decf0d";
    public static final String PLAIN_PUSH_DATA_ENCRYPTION_KEY = "f72896dc308292f6aa26711d3331e65c";
    public static final String PLAIN_PUSH_DATA_MAC_KEY = "af6b62a55df6bc0ab76a50ce18088b54";
    public static final String PLAIN_PUSH_MESSAGE_MAC_KEY = "c7142575feb4ebd80e43f4b4d75ffe88";
    public static final String PLAIN_ATTESTATION_ENCRYPTION_KEY = "acfa5a29334dd171423061574e4fabea";
    public static final String PLAIN_ATTESTATION_MAC_KEY = "8232e9fd71e7ba4db3d404934d3cfc83";
    public static final String PLAIN_TRANSACTION_ENCRYPTION_KEY = "82af0a11de94f139cee142bf3f60cc42";
    public static final String PLAIN_TRANSACTION_MAC_KEY = "6790068c9db7d51b8d7371f8f21d84e0";
    public static final String PLAIN_SECRET_KEY = "7ba90d2e4d5e40341edfe5302aa9bfd5";
    public static final String SECRETKEY_FINGERPRINT = "0371f80a4149e86a263f233efb3a18ff911613e43f862b7e0813598bbac93fec";
    public static final String PLAIN_RGK_ENC_KEY = "2611146d09b00c4d08c7c9604c820c92";
    public static final String PLAIN_RGK_MAC_KEY = "795be9c7ef60454ee97d1bbde60b7d98";

    // sdk
    public static final String PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100a1eb2bb2453691d721240f724dc00d4575c65d782dc05b647d4132c9d829964462e037e22720308f36c501ead79e631df930b3fab1e04e6322a82ebb49af0d0f09b06c3f1972b1ed04b66d668af5a07fbfd535c4eb23dd064a8a8708b9c95cf4d80fc7cbef80cf2d6d5115583bc6b1331c31faa62bcc93f2a2fba154264bc4cbe00493c3cfb40584542a70c290c1b16535559433c5904d8ba023f3d75fd3562880cd5b1ced844232698c5ae014de2d9b0b6a4f35b6bcc5c09b3189268b91197dc9e251955a1471ac1fbd59d49e4cb4964f453098f9f1c140f3be5a337f931d6093ba1493cd05dc8a169bd5a87105e437212f886a537b1cdb14429857e63ea3270203010001";
    public static final String SDK_ID = "POS";
    public static final String SDK_INSTANCE_ID = "771E7929535CA0DB0A63F45194B845CD";
    public static final String FCM_REGISTRATION_ID = "APA91bHPRgkF3JUikC4ENAHEeMrd41Zxv3hVZjC9KtT8OvPVG";
    public static final BigDecimal LATITUDE = new BigDecimal("56.9769399");
    public static final BigDecimal LONGITUDE = new BigDecimal("24.1655157");

    public static Sdk getTestSdk() {
        return new Sdk(
                SDK_ID,
                SDK_INSTANCE_ID,
                PUBLIC_KEY,
                FCM_REGISTRATION_ID,
                new GpsLocation(LATITUDE, LONGITUDE),
                getTestPlainKeyset(),
                getTestEncryptedKeyset()
        );
    }

    public static PlainSdkKeyset getTestPlainKeyset() {
        return new PlainSdkKeyset(
                KEYSET_ID,
                new PlainPushKeys(PLAIN_PUSH_DATA_ENCRYPTION_KEY, PLAIN_PUSH_DATA_MAC_KEY, PLAIN_PUSH_MESSAGE_MAC_KEY),
                new PlainEncMacKeys(PLAIN_ATTESTATION_ENCRYPTION_KEY, PLAIN_ATTESTATION_MAC_KEY),
                new PlainEncMacKeys(PLAIN_TRANSACTION_ENCRYPTION_KEY, PLAIN_TRANSACTION_MAC_KEY),
                new PlainSecretKey(PLAIN_SECRET_KEY, SECRETKEY_FINGERPRINT)
        );
    }

    public static EncryptedSdkKeyset getTestEncryptedKeyset() {
        return new EncryptedSdkKeyset(
                KEYSET_ID,
                new EncryptedPushKeys(ENC_PUSH_DATA_ENCRYPTION_KEY_UNDER_RGK, ENC_PUSH_DATA_MAC_KEY_UNDER_RGK, ENC_PUSH_MESSAGE_MAC_KEY_UNDER_RGK),
                new EncryptedEncMacKeys(ENC_ATTESTATION_ENCRYPTION_KEY_UNDER_RGK, ENC_ATTESTATION_MAC_KEY_UNDER_RGK),
                new EncryptedEncMacKeys(ENC_TRANSACTION_ENCRYPTION_KEY_UNDER_RGK, ENC_TRANSACTION_MAC_KEY_UNDER_RGK),
                new EncryptedSecretKey(ENC_SECRET_KEY_UNDER_RGK, SECRETKEY_FINGERPRINT)
        );
    }

    public static EncryptedRgk getEncryptedRgk() {
        return new EncryptedRgk(ENC_RGK_ENC_KEY_UNDER_SHARED, ENC_RGK_MAC_KEY_UNDER_SHARED);
    }

    public static PlainRgk getPlainRgk() {
        return new PlainRgk(decode(PLAIN_RGK_ENC_KEY), decode(PLAIN_RGK_MAC_KEY));
    }

    @SneakyThrows
    private static byte[] decode(String hex) {
        return Hex.decodeHex(hex);
    }
}
