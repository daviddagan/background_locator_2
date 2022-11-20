package yukams.app.background_locator_2.models.wcdma;


import androidx.annotation.Keep;

import yukams.app.background_locator_2.models.common.Cell;
import yukams.app.background_locator_2.models.tdscdma.BandTDSCDMA;
import yukams.app.background_locator_2.models.tdscdma.SignalTDSCDMA;

import java.io.Serializable;
@Keep
public class CellWCDMA extends Cell implements Serializable {

    private Integer ci;
    private Integer lac;
    private Integer psc;
    private Integer cid;
    private Integer rnc;
    private String cgi;

    private BandWCDMA bandWCDMA;
    private SignalWCDMA signalWCDMA;

    public CellWCDMA() {
    }

    public Integer getCi() {
        return ci;
    }

    public void setCi(Integer ci) {
        this.ci = ci;
    }

    public Integer getLac() {
        return lac;
    }

    public void setLac(Integer lac) {
        this.lac = lac;
    }

    public Integer getPsc() {
        return psc;
    }

    public void setPsc(Integer psc) {
        this.psc = psc;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getRnc() {
        return rnc;
    }

    public void setRnc(Integer rnc) {
        this.rnc = rnc;
    }

    public String getCgi() {
        return cgi;
    }

    public void setCgi(String cgi) {
        this.cgi = cgi;
    }

    public BandWCDMA getBandWCDMA() {
        return bandWCDMA;
    }

    public void setBandWCDMA(BandWCDMA bandWCDMA) {
        this.bandWCDMA = bandWCDMA;
    }

    public SignalWCDMA getSignalWCDMA() {
        return signalWCDMA;
    }

    public void setSignalWCDMA(SignalWCDMA signalWCDMA) {
        this.signalWCDMA = signalWCDMA;
    }
}

