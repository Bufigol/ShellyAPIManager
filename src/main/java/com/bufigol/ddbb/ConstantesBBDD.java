package com.bufigol.ddbb;

public class ConstantesBBDD {

    // Inserts para la tabla energy_meter
    public static final String INSERT_ENERGY_METER = "INSERT INTO energy_meter (fase_a_act_power, fase_a_aprt_power, fase_a_current, fase_a_freq, fase_a_pf, fase_a_voltage, "
            + "fase_b_act_power, fase_b_aprt_power, fase_b_current, fase_b_freq, fase_b_pf, fase_b_voltage, "
            + "fase_c_act_power, fase_c_aprt_power, fase_c_current, fase_c_freq, fase_c_pf, fase_c_voltage, "
            + "total_act_power, total_aprt_power, total_current, user_calibrated_phases) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    // Inserts para la tabla temperature
    public static final String INSERT_TEMPERATURE = "INSERT INTO temperature (celsius, fahrenheit) VALUES (?, ?)";

    // Inserts para la tabla energy_meter_data
    public static final String INSERT_ENERGY_METER_DATA = "INSERT INTO energy_meter_data (a_total_act_energy, a_total_act_ret_energy, "
            + "b_total_act_energy, b_total_act_ret_energy, c_total_act_energy, c_total_act_ret_energy, "
            + "total_act_energy, total_act_ret_energy) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    // Inserts para la tabla wifi_status
    public static final String INSERT_WIFI_STATUS = "INSERT INTO wifi_status (sta_ip, status, ssid, rssi) VALUES (?, ?, ?, ?)";

    // Inserts para la tabla available_updates
    public static final String INSERT_AVAILABLE_UPDATES = "INSERT INTO available_updates (stable_version) VALUES (?)";

    // Inserts para la tabla cloud_status
    public static final String INSERT_CLOUD_STATUS = "INSERT INTO cloud_status (connected) VALUES (?)";

    // Inserts para la tabla device_status
    public static final String INSERT_DEVICE_STATUS = "INSERT INTO device_status (code, em0_id, updated, cloud_connected, wifi_sta_ip, wifi_status, "
            + "wifi_ssid, wifi_rssi, temperature0_id, emdata0_id, sys_mac, sys_restart_required, sys_time, "
            + "sys_uptime, sys_ram_size, sys_ram_free, sys_fs_size, sys_fs_free, sys_cfg_rev, sys_kvs_rev, "
            + "sys_schedule_rev, sys_webhook_rev, sys_reset_reason) "
            + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
}