package yukams.app.background_locator_2.models;

import androidx.annotation.Keep;

import yukams.app.background_locator_2.models.cdma.CellCDMA;
import yukams.app.background_locator_2.models.gsm.CellGSM;
import yukams.app.background_locator_2.models.lte.CellLTE;
import yukams.app.background_locator_2.models.nr.CellNR;
import yukams.app.background_locator_2.models.tdscdma.CellTDSCDMA;
import yukams.app.background_locator_2.models.wcdma.CellWCDMA;

import java.io.Serializable;
@Keep
public class CellType implements Serializable {

    private String type;
    private CellCDMA cdma;
    private CellGSM gsm;
    private CellLTE lte;
    private CellNR nr;
    private CellTDSCDMA tdscdma;
    private CellWCDMA wcdma;

    public CellType() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CellCDMA getCdma() {
        return cdma;
    }

    public void setCdma(CellCDMA cdma) {
        this.cdma = cdma;
    }

    public CellGSM getGsm() {
        return gsm;
    }

    public void setGsm(CellGSM gsm) {
        this.gsm = gsm;
    }

    public CellLTE getLte() {
        return lte;
    }

    public void setLte(CellLTE lte) {
        this.lte = lte;
    }

    public CellNR getNr() {
        return nr;
    }

    public void setNr(CellNR nr) {
        this.nr = nr;
    }

    public CellTDSCDMA getTdscdma() {
        return tdscdma;
    }

    public void setTdscdma(CellTDSCDMA tdscdma) {
        this.tdscdma = tdscdma;
    }

    public CellWCDMA getWcdma() {
        return wcdma;
    }

    public void setWcdma(CellWCDMA wcdma) {
        this.wcdma = wcdma;
    }
}
