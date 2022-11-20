package yukams.app.background_locator_2.cellInfo.models.cdma;

import androidx.annotation.Keep;

import yukams.app.background_locator_2.cellInfo.models.common.Signal;

import java.io.Serializable;
@Keep
public class SignalCDMA extends Signal implements Serializable {

    private int cdmaRssi;
    private int evdoRssi;
    private int evdoSnr;
    private double cdmaEcio;
    private double evdoEcio;


    public SignalCDMA() {
    }

    public int getCdmaRssi() {
        return cdmaRssi;
    }

    public void setCdmaRssi(int cdmaRssi) {
        this.cdmaRssi = cdmaRssi;
    }

    public int getEvdoRssi() {
        return evdoRssi;
    }

    public void setEvdoRssi(int evdoRssi) {
        this.evdoRssi = evdoRssi;
    }

    public int getEvdoSnr() {
        return evdoSnr;
    }

    public void setEvdoSnr(int evdoSnr) {
        this.evdoSnr = evdoSnr;
    }

    public double getCdmaEcio() {
        return cdmaEcio;
    }

    public void setCdmaEcio(double cdmaEcio) {
        this.cdmaEcio = cdmaEcio;
    }

    public double getEvdoEcio() {
        return evdoEcio;
    }

    public void setEvdoEcio(double evdoEcio) {
        this.evdoEcio = evdoEcio;
    }
}
