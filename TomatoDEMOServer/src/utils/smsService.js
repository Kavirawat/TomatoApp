/**
 * Real SMS Dispatcher Service for Tomato
 * Supports:
 * 1. Fast2SMS (Indian OTP route)
 * 2. 2Factor.in (Indian SMS gateway)
 * 3. Twilio (Global SMS)
 * 4. Generic SMS Gateway URL with {phone} and {otp} placeholders
 */

export async function sendSmsOtp(phone, otp) {
  const cleanPhone = String(phone).replace(/\D/g, "").slice(-10);
  const fullPhone = `91${cleanPhone}`;

  console.log(`\n======================================================`);
  console.log(`📱 [SMS SERVICE] Initiating SMS dispatch for: +91 ${cleanPhone}`);
  console.log(`🔑 [SMS SERVICE] OTP Code: ${otp}`);

  // 1. Check Fast2SMS
  if (process.env.FAST2SMS_API_KEY) {
    try {
      console.log(`🌐 [SMS SERVICE] Sending via Fast2SMS gateway...`);
      const response = await fetch("https://www.fast2sms.com/dev/bulkV2", {
        method: "POST",
        headers: {
          "authorization": process.env.FAST2SMS_API_KEY,
          "Content-Type": "application/json"
        },
        body: JSON.stringify({
          route: "otp",
          variables_values: String(otp),
          numbers: cleanPhone
        })
      });
      const data = await response.json();
      console.log(`📡 [Fast2SMS Response]:`, data);
      if (data && (data.return === true || data.status_code === 200)) {
        console.log(`✅ [SMS SERVICE] Fast2SMS delivered successfully to +91 ${cleanPhone}`);
        console.log(`======================================================\n`);
        return { success: true, provider: "Fast2SMS", details: data };
      }
    } catch (err) {
      console.error(`❌ [SMS SERVICE] Fast2SMS error:`, err.message);
    }
  }

  // 2. Check 2Factor.in
  if (process.env.TWO_FACTOR_API_KEY) {
    try {
      console.log(`🌐 [SMS SERVICE] Sending via 2Factor.in gateway...`);
      const url = `https://2factor.in/API/V1/${process.env.TWO_FACTOR_API_KEY}/SMS/${cleanPhone}/${otp}/OTP1`;
      const response = await fetch(url);
      const data = await response.json();
      console.log(`📡 [2Factor Response]:`, data);
      if (data && (data.Status === "Success" || data.status === "success")) {
        console.log(`✅ [SMS SERVICE] 2Factor delivered successfully to +91 ${cleanPhone}`);
        console.log(`======================================================\n`);
        return { success: true, provider: "2Factor", details: data };
      }
    } catch (err) {
      console.error(`❌ [SMS SERVICE] 2Factor error:`, err.message);
    }
  }

  // 3. Check Twilio
  if (process.env.TWILIO_ACCOUNT_SID && process.env.TWILIO_AUTH_TOKEN && process.env.TWILIO_FROM_NUMBER) {
    try {
      console.log(`🌐 [SMS SERVICE] Sending via Twilio gateway...`);
      const twilioUrl = `https://api.twilio.com/2010-04-01/Accounts/${process.env.TWILIO_ACCOUNT_SID}/Messages.json`;
      const auth = Buffer.from(`${process.env.TWILIO_ACCOUNT_SID}:${process.env.TWILIO_AUTH_TOKEN}`).toString("base64");
      const fromNumber = process.env.TWILIO_FROM_NUMBER.trim().startsWith("+")
        ? process.env.TWILIO_FROM_NUMBER.trim()
        : `+${process.env.TWILIO_FROM_NUMBER.trim()}`;

      const bodyParams = new URLSearchParams({
        To: `+91${cleanPhone}`,
        From: fromNumber,
        Body: `Your Tomato login OTP is ${otp}. Valid for 10 minutes. Please do not share it with anyone.`
      });

      const response = await fetch(twilioUrl, {
        method: "POST",
        headers: {
          "Authorization": `Basic ${auth}`,
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: bodyParams.toString()
      });
      const data = await response.json();
      console.log(`📡 [Twilio Response]:`, data);
      if (data && data.sid) {
        console.log(`✅ [SMS SERVICE] Twilio delivered successfully to +91 ${cleanPhone}`);
        console.log(`======================================================\n`);
        return { success: true, provider: "Twilio", sid: data.sid };
      }
    } catch (err) {
      console.error(`❌ [SMS SERVICE] Twilio error:`, err.message);
    }
  }

  // 4. Check Generic Gateway URL (e.g. SMS_GATEWAY_URL=https://my-sms.com/send?phone={phone}&otp={otp})
  if (process.env.SMS_GATEWAY_URL) {
    try {
      console.log(`🌐 [SMS SERVICE] Sending via generic SMS Gateway URL...`);
      const targetUrl = process.env.SMS_GATEWAY_URL
        .replace(/\{phone\}/g, cleanPhone)
        .replace(/\{otp\}/g, otp);

      const response = await fetch(targetUrl);
      const text = await response.text();
      console.log(`📡 [Custom Gateway Response]:`, text);
      console.log(`✅ [SMS SERVICE] Custom gateway dispatched to +91 ${cleanPhone}`);
      console.log(`======================================================\n`);
      return { success: true, provider: "CustomGateway", response: text };
    } catch (err) {
      console.error(`❌ [SMS SERVICE] Custom gateway error:`, err.message);
    }
  }

  // If no SMS provider key configured in .env:
  console.log(`⚠️ [SMS SERVICE] Notice: No real SMS Gateway API key configured in .env.`);
  console.log(`ℹ️ [SMS SERVICE] To receive real SMS directly on your phone:`);
  console.log(`   Add FAST2SMS_API_KEY, TWO_FACTOR_API_KEY, or TWILIO credentials in TomatoDEMOServer/.env`);
  console.log(`🔑 [SMS SERVICE] Current OTP for mobile number +91 ${cleanPhone} is: ${otp}`);
  console.log(`======================================================\n`);

  return {
    success: false,
    needsGatewayConfig: true,
    message: `No SMS gateway configured in .env. Console OTP is ${otp}`
  };
}
