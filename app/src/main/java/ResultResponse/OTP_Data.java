
package ResultResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OTP_Data {

    @SerializedName("otp")
    @Expose
    private String otp;

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

}
