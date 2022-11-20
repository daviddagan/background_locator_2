package yukams.app.background_locator_2.cellInfo.models.wcdma;

import androidx.annotation.Keep;

import yukams.app.background_locator_2.cellInfo.models.common.Band;

import java.io.Serializable;
@Keep
public class BandWCDMA extends Band implements Serializable {

    private int downlinkUarfcn;

    public BandWCDMA() {
    }

    public int getDownlinkUarfcn() {
        return downlinkUarfcn;
    }

    public void setDownlinkUarfcn(int downlinkUarfcn) {
        this.downlinkUarfcn = downlinkUarfcn;
    }
    
}
