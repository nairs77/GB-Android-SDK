package com.gebros.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.joycity.platform.sdk.JoypleSettings;
import com.joycity.platform.sdk.auth.ProfileApi;
import com.joycity.platform.sdk.listener.JoypleInAppListener;
import com.joycity.platform.sdk.log.JLog;
import com.joycity.platform.sdk.pay.IabPurchase;
import com.joycity.platform.sdk.pay.IabResult;
import com.joycity.platform.sdk.pay.JoypleInAppItem;
import com.joycity.platform.sdk.pay.JoypleInAppManager;
import com.joycity.platform.sdk.util.JoypleMessageUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joycity-Platform on 5/30/16.
 */
public class InAppFragment extends Fragment implements View.OnClickListener, Spinner.OnItemSelectedListener {

    private static final String TAG = InAppFragment.class.getSimpleName();

    private List<JoypleInAppItem> items = new ArrayList<JoypleInAppItem>()

    {
        {
            add(new JoypleInAppItem("0.55", "com.hoga.fwsd.30_gem", "30Gem", "inapp"));
            add(new JoypleInAppItem("1.00", "com.hoga.fwsd.50_gem", "50Gem", "inapp"));
            add(new JoypleInAppItem("1.50", "com.hoga.fwsd.100_gem", "100Gem", "inapp"));
            add(new JoypleInAppItem("1.55", "com.hoga.fwsd.300_gem", "300Gem", "inapp"));
            add(new JoypleInAppItem("1.99", "com.hoga.fwsd.500_gem", "500Gem", "inapp"));
            add(new JoypleInAppItem("1", "com.hoga.fwsd.1000_gem", "1000Gem", "inapp"));
        }
    };

    /**
     * Called to have the fragment instantiate its user interface view.
     * This is optional, and non-graphical fragments can return null (which
     * is the default implementation).  This will be called between
     * {@link #onCreate(Bundle)} and {@link #onActivityCreated(Bundle)}.
     * <p/>
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return Return the View for the fragment's UI, or null.
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inAppView = inflater.inflate(R.layout.activity_inapp, container, false);

        String platformName = JoypleSettings.getPlatformType().getName();
        mPlatformTitle = (TextView)inAppView.findViewById(R.id.login_platform_title);
        mPlatformTitle.setText(platformName);

        mBtnBuyItem = (Button)inAppView.findViewById(R.id.btn_buy);
        mBtnBuyItem.setOnClickListener(this);

        List<String> itemList = new ArrayList<String>();
        itemList.add("Name (product id : com.hoga.fwsd.xxx) - Price");
        for (JoypleInAppItem item : items) {
            itemList.add(item.getItemName() + "(" + item.getSku() + ")" + "-" + item.getPrice() + "RMB");
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, itemList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mSpItems = (Spinner)inAppView.findViewById(R.id.spinner_items);
        mSpItems.setOnItemSelectedListener(this);
        mSpItems.setAdapter(spinnerAdapter);

        return inAppView;
    }

    @Override
    public void onClick(View view) {
        int clickedViewId = view.getId();

        if (clickedViewId == mBtnBuyItem.getId()) {
            RestoreItems();
            JoypleInAppManager.InitInAppService(ProfileApi.getLocalUser().getUserKey(), new JoypleInAppListener.OnIabSetupFinishedListener() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getActivity(), "[InitInAppService onSuccess]", Toast.LENGTH_LONG).show();
                    BuyItem();
                }

                @Override
                public void onFail() {
                    Toast.makeText(getActivity(), "[InitInAppService onFail]", Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {

        if (pos == 0)
            return;

        mCurrentItem = items.get(pos - 1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }


    private void BuyItem() {
        JLog.d(TAG + " BuyItem::::::::::");
        JoypleMessageUtils.toast(getActivity(), "자~~ 구매 합니다.");
        JoypleInAppManager.BuyItem(getActivity(), "", mCurrentItem, new JoypleInAppListener.OnPurchaseFinishedListener() {
            @Override
            public void onSuccess(IabPurchase purchaseInfo) {
                JLog.d(TAG + "PurchaseInfo = %s", purchaseInfo.getPaymentKey());
                JoypleMessageUtils.alert(getActivity(), String.format("Success !! \n paymentKey = %s", purchaseInfo.getPaymentKey()));
                Toast.makeText(getActivity(), "[BuyItem onSuccess]purchaseInfo = "+purchaseInfo.getPaymentKey(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFail(IabResult result) {
                JLog.d(TAG + "Purchasing Failed!!! Error Code = %d , message = %s", result.getResponse(), result.getMessage());
                Toast.makeText(getActivity(), "[BuyItem onFail]errorCode = " + result.getResponse()+" , errorMsd = " + result.getMessage(), Toast.LENGTH_LONG).show();
            //    JoypleMessageUtils.alert(getActivity(), String.format("Purchasing Failed!!! Error Code (%d), message = %s", result.getResponse(), result.getMessage()));
            }

            @Override
            public void onCancel(boolean isUserCancelled) {
                JoypleMessageUtils.alert(getActivity(), String.format("Purchasing Failed!!!User Cancelled::"+isUserCancelled));
                Toast.makeText(getActivity(), "[BuyItem onCancel]isUserCancelled = "+isUserCancelled, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void RestoreItems() {
        JLog.d(TAG + " RestoreItems::::::::::");
        JoypleMessageUtils.toast(getActivity(), "구매 전에 복구 데이터 부터 확인 합니다!!!");
        JoypleInAppManager.ReStoreItems(new JoypleInAppListener.OnRestoreItemsFinishedListener() {
            @Override
            public void onSuccess(final List<String> paymentKeys) {
                JLog.d(TAG + "Restore items = %s", paymentKeys.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "[RestoreItems onSuccess]Restore items = "+paymentKeys.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onFail(final IabResult result) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JoypleMessageUtils.alert(getActivity(), result.getMessage());
                        Toast.makeText(getActivity(), "[RestoreItems onFail]errorCode = "+result.getResponse() + " , errorMsg = " + result.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }

    private TextView mPlatformTitle;
    private Button mBtnBuyItem;
    private Spinner mSpItems;
    private JoypleInAppItem mCurrentItem;
}
