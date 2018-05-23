package com.miaodao.Fragment.Product.bean;

/**
 * Created by daixinglong on 2017/5/13.
 */

public class AuthInfo {

    private IdAuthState A100000;
    private CardAuthState A101000;
    private AddrAuthState A102000;
    private PhoneAuthState A103000;
    private VideoAuthState A104000;
    private VideoCompare A104001;
    private AuthState authInfo;

    public AuthInfo() {
    }

    public IdAuthState getA100000() {
        return A100000 == null ? new IdAuthState() : A100000;
    }

    public void setA100000(IdAuthState a100000) {
        A100000 = a100000;
    }

    public CardAuthState getA101000() {
        return A101000 == null ? new CardAuthState() : A101000;
    }

    public void setA101000(CardAuthState a101000) {
        A101000 = a101000;
    }

    public AddrAuthState getA102000() {
        return A102000 == null ? new AddrAuthState() : A102000;
    }

    public void setA102000(AddrAuthState a102000) {
        A102000 = a102000;
    }

    public PhoneAuthState getA103000() {
        return A103000 == null ? new PhoneAuthState() : A103000;
    }

    public void setA103000(PhoneAuthState a103000) {
        A103000 = a103000;
    }

    public VideoAuthState getA104000() {
        return A104000 == null ? new VideoAuthState() : A104000;
    }

    public void setA104000(VideoAuthState a104000) {
        A104000 = a104000;
    }

    public AuthState getAuthInfo() {
        return authInfo == null ? new AuthState() : authInfo;
    }

    public void setAuthInfo(AuthState authInfo) {
        this.authInfo = authInfo;
    }

    public VideoCompare getA104001() {
        return A104001 == null ? new VideoCompare() : A104001;
    }

    public void setA104001(VideoCompare a104001) {
        A104001 = a104001;
    }
}
