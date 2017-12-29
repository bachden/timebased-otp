package nhb.security.otp.rfc6328.test;

import java.io.IOException;
import java.util.Base64;

import com.google.zxing.WriterException;

import nhb.security.otp.rfc6238.Secret;
import nhb.security.otp.rfc6238.TOTP;
import nhb.utils.QRCodeGenerator;

public class TestOTP {

	private static final String base64ImagePrefix = "data:image/jpeg;base64, ";

	public static String generateOTPAuth(String account, String secretBase32, String issuer) {
		return String.format("otpauth://totp/%s?secret=%s&issuer=%s", EncodingUtils.encodeURIComponent(account),
				EncodingUtils.encodeURIComponent(secretBase32), EncodingUtils.encodeURIComponent(issuer));
	}

	public static void main(String[] args) throws WriterException, IOException, InterruptedException {
		byte[] secret = Secret.generate();

		// generate Google Authenticator QR Code
		String encoded = Secret.toBase32(secret);

		String qrString = generateOTPAuth("Test:Bach Hoang Nguyen", encoded, "Test");

		System.out.println("QR String: " + qrString);

		byte[] bytes = QRCodeGenerator.createQRImage(qrString, 240);
		String qrBase64 = base64ImagePrefix + Base64.getEncoder().encodeToString(bytes);

		System.out.println(qrBase64);

		System.out.println("Secret: " + encoded);

		TOTP totp = new TOTP();
		while (true) {
			System.out.println("current otp: " + totp.generate(secret));
			Thread.sleep(1000);
		}
	}

}
