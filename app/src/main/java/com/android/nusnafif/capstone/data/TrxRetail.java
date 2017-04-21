package com.android.nusnafif.capstone.data;

/**
 * Created by NUSNAFIF on 3/25/2017.
 */

public class TrxRetail {
    String id;
    String dtTrx;
    String storeName;
    String storeDevice;
    String actionIntent;
    String subscriberEmail;
    String subscriberPhone;
    String subscriberRewards;

    public TrxRetail(String id, String dtTrx, String storeName, String storeDevice, String actionIntent, String subscriberEmail, String subscriberPhone, String subscriberRewards) {
        this.id = id;
        this.dtTrx = dtTrx;
        this.storeName = storeName;
        this.storeDevice = storeDevice;
        this.actionIntent = actionIntent;
        this.subscriberEmail = subscriberEmail;
        this.subscriberPhone = subscriberPhone;
        this.subscriberRewards = subscriberRewards;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDtTrx() {
        return dtTrx;
    }

    public void setDtTrx(String dtTrx) {
        this.dtTrx = dtTrx;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreDevice() {
        return storeDevice;
    }

    public void setStoreDevice(String storeDevice) {
        this.storeDevice = storeDevice;
    }

    public String getActionIntent() {
        return actionIntent;
    }

    public void setActionIntent(String actionIntent) {
        this.actionIntent = actionIntent;
    }

    public String getSubscriberEmail() {
        return subscriberEmail;
    }

    public void setSubscriberEmail(String subscriberEmail) {
        this.subscriberEmail = subscriberEmail;
    }

    public String getSubscriberPhone() {
        return subscriberPhone;
    }

    public void setSubscriberPhone(String subscriberPhone) {
        this.subscriberPhone = subscriberPhone;
    }

    public String getSubscriberRewards() {
        return subscriberRewards;
    }

    public void setSubscriberRewards(String subscriberRewards) {
        this.subscriberRewards = subscriberRewards;
    }

}
