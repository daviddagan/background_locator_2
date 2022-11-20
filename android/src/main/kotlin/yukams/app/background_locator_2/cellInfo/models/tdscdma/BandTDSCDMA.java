package yukams.app.background_locator_2.models.tdscdma;

import androidx.annotation.Keep;

import yukams.app.background_locator_2.models.common.Band;

import java.io.Serializable;
@Keep
public class BandTDSCDMA extends Band implements Serializable {

    private int downlinkUarfcn;

    public BandTDSCDMA() {
    }

    public int getDownlinkUarfcn() {
        return downlinkUarfcn;
    }

    public void setDownlinkUarfcn(int downlinkUarfcn) {
        this.downlinkUarfcn = downlinkUarfcn;
    }
    
}
