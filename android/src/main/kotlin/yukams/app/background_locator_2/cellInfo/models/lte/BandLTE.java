package yukams.app.background_locator_2.models.lte;

import androidx.annotation.Keep;

import yukams.app.background_locator_2.models.common.Band;

import java.io.Serializable;
@Keep
public class BandLTE extends Band implements Serializable {

    private int downlinkEarfcn;

    public BandLTE() {
    }

    public int getDownlinkEarfcn() {
        return downlinkEarfcn;
    }

    public void setDownlinkEarfcn(int downlinkEarfcn) {
        this.downlinkEarfcn = downlinkEarfcn;
    }
}
