package com.nic.tnebnewwatersource.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;

import java.util.List;

/**
 * Created by shanmugapriyan on 25/05/16.
 */
public class CommonAdapter extends BaseAdapter {
    private List<TNEBSystem> TNEBSystems;
    private Context mContext;
    private String type;
    private PrefManager prefManager;


    public CommonAdapter(Context mContext, List<TNEBSystem> TNEBSystems, String type) {
        this.TNEBSystems = TNEBSystems;
        this.mContext = mContext;
        prefManager = new PrefManager(mContext);
        this.type = type;
    }


    public int getCount() {
        return TNEBSystems.size();
    }


    public Object getItem(int position) {
        return position;
    }


    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.spinner_drop_down_item, parent, false);
//        TextView tv_type = (TextView) view.findViewById(R.id.tv_spinner_item);
        View view = inflater.inflate(R.layout.spinner_value, parent, false);
        TextView tv_type = (TextView) view.findViewById(R.id.spinner_list_value);
        TNEBSystem TNEBSystem = TNEBSystems.get(position);

        if (type.equalsIgnoreCase("VillageList")) {
            tv_type.setText(TNEBSystem.getPvName());
        }
        else if (type.equalsIgnoreCase("HabitationList")) {
            /*if(prefManager.getLocalLanguage().equals("en")) {
                tv_type.setText(TNEBSystem.getHabitation_name());
            }
            else if(prefManager.getLocalLanguage().equals("ta")) {
                tv_type.setText(TNEBSystem.getHabitation_name_ta());
            }*/
            tv_type.setText(TNEBSystem.getHabitation_name());
        }
        else if (type.equalsIgnoreCase("HorsePowerList")) {
            tv_type.setText(TNEBSystem.getHorse_power());
        }
        else if (type.equalsIgnoreCase("MotorTypeList")) {
            tv_type.setText(TNEBSystem.getMotor_type());
        }
        else if (type.equalsIgnoreCase("MiniMotorTypeList")) {
            tv_type.setText(TNEBSystem.getMotor_type());
        }
        else if (type.equalsIgnoreCase("GlrMotorTypeList")) {
            tv_type.setText(TNEBSystem.getMotor_type());
        }
        else if (type.equalsIgnoreCase("MiniPowerPumpMotorTypeList")) {
            tv_type.setText(TNEBSystem.getMotor_type());
        }
        else if (type.equalsIgnoreCase("MiniWithOutOhtMotorTypeList")) {
            tv_type.setText(TNEBSystem.getMotor_type());
        }
        else if (type.equalsIgnoreCase("TNEBMiniOhtHorsePower")) {
            tv_type.setText(TNEBSystem.getTneb_horse_power());
        }
        else if (type.equalsIgnoreCase("TNEBOhtHorsePower")) {
            tv_type.setText(TNEBSystem.getTneb_horse_power());
        }
        else if (type.equalsIgnoreCase("TNEBGLRHorsePowerList")) {
            tv_type.setText(TNEBSystem.getTneb_horse_power());
        }
        else if (type.equalsIgnoreCase("TNEBMiniPowerPumpHorsePowerList")) {
            tv_type.setText(TNEBSystem.getTneb_horse_power());
        }
        else if (type.equalsIgnoreCase("TNEBMiniWithOutOhtHorsePowerList")) {
            tv_type.setText(TNEBSystem.getTneb_horse_power());
        }
        else if (type.equalsIgnoreCase("TNEBOhtTankCapacityList")) {
            tv_type.setText(TNEBSystem.getTneb_tank_capacity());
        }
        else if (type.equalsIgnoreCase("TNEBMiniOhtTankCapacityList")) {
            tv_type.setText(TNEBSystem.getTneb_tank_capacity());
        }
        else if (type.equalsIgnoreCase("TNEBGLRTankCapacityList")) {
            tv_type.setText(TNEBSystem.getTneb_tank_capacity());
        }
        else if (type.equalsIgnoreCase("TNEBMiniPowerPumpTankCapacityList")) {
            tv_type.setText(TNEBSystem.getTneb_tank_capacity());
        }
        else if (type.equalsIgnoreCase("water_supplied_reason")) {
            tv_type.setText(TNEBSystem.getReason_for_supply());
        }
        else if (type.equalsIgnoreCase("water_supplied_throughList")) {
            tv_type.setText(TNEBSystem.getWater_supply_through());
        }
        else if (type.equalsIgnoreCase("waterSourceTypeList")) {
            tv_type.setText(TNEBSystem.getWater_source_type_name());
        }
        else if (type.equalsIgnoreCase("connectionPurposeList")) {
            if(prefManager.getLocalLanguage().equals("en")) {
                tv_type.setText(TNEBSystem.getConncetion_name());
            }
            else {
                tv_type.setText(TNEBSystem.getConnection_name_ta());
            }
        }
        return view;
    }
}
