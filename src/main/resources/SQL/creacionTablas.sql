CREATE TABLE IF NOT EXISTS energy_meter (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            fase_a_act_power DECIMAL(10, 2),
                                            fase_a_aprt_power DECIMAL(10, 2),
                                            fase_a_current DECIMAL(10, 2),
                                            fase_a_freq DECIMAL(10, 2),
                                            fase_a_pf DECIMAL(10, 2),
                                            fase_a_voltage DECIMAL(10, 2),
                                            fase_b_act_power DECIMAL(10, 2),
                                            fase_b_aprt_power DECIMAL(10, 2),
                                            fase_b_current DECIMAL(10, 2),
                                            fase_b_freq DECIMAL(10, 2),
                                            fase_b_pf DECIMAL(10, 2),
                                            fase_b_voltage DECIMAL(10, 2),
                                            fase_c_act_power DECIMAL(10, 2),
                                            fase_c_aprt_power DECIMAL(10, 2),
                                            fase_c_current DECIMAL(10, 2),
                                            fase_c_freq DECIMAL(10, 2),
                                            fase_c_pf DECIMAL(10, 2),
                                            fase_c_voltage DECIMAL(10, 2),
                                            total_act_power DECIMAL(10, 2),
                                            total_aprt_power DECIMAL(10, 2),
                                            total_current DECIMAL(10, 2),
                                            user_calibrated_phases BOOLEAN
);

CREATE TABLE IF NOT EXISTS temperature (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           celsius DECIMAL(5, 2),
                                           fahrenheit DECIMAL(5, 2)
);

CREATE TABLE IF NOT EXISTS energy_meter_data (
                                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                                 a_total_act_energy DECIMAL(10, 2),
                                                 a_total_act_ret_energy DECIMAL(10, 2),
                                                 b_total_act_energy DECIMAL(10, 2),
                                                 b_total_act_ret_energy DECIMAL(10, 2),
                                                 c_total_act_energy DECIMAL(10, 2),
                                                 c_total_act_ret_energy DECIMAL(10, 2),
                                                 total_act_energy DECIMAL(10, 2),
                                                 total_act_ret_energy DECIMAL(10, 2)
);

CREATE TABLE IF NOT EXISTS wifi_status (
                                           id INT AUTO_INCREMENT PRIMARY KEY,
                                           sta_ip VARCHAR(15),
                                           status VARCHAR(50),
                                           ssid VARCHAR(100),
                                           rssi INT
);

CREATE TABLE IF NOT EXISTS available_updates (
                                                 id INT AUTO_INCREMENT PRIMARY KEY,
                                                 stable_version VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS cloud_status (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            connected BOOLEAN
);

CREATE TABLE IF NOT EXISTS device_status (
                                             id INT AUTO_INCREMENT PRIMARY KEY,
                                             code VARCHAR(50),
                                             em0_id INT,
                                             updated DATETIME,
                                             cloud_connected BOOLEAN,
                                             wifi_sta_ip VARCHAR(15),
                                             wifi_status VARCHAR(50),
                                             wifi_ssid VARCHAR(100),
                                             wifi_rssi INT,
                                             temperature0_id INT,
                                             emdata0_id INT,
                                             sys_mac VARCHAR(17),
                                             sys_restart_required BOOLEAN,
                                             sys_time DATETIME,
                                             sys_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             sys_uptime INT,
                                             sys_ram_size INT,
                                             sys_ram_free INT,
                                             sys_fs_size INT,
                                             sys_fs_free INT,
                                             sys_cfg_rev VARCHAR(50),
                                             sys_kvs_rev VARCHAR(50),
                                             sys_schedule_rev VARCHAR(50),
                                             sys_webhook_rev VARCHAR(50),
                                             sys_reset_reason VARCHAR(255),
                                             FOREIGN KEY (em0_id) REFERENCES energy_meter(id),
                                             FOREIGN KEY (temperature0_id) REFERENCES temperature(id),
                                             FOREIGN KEY (emdata0_id) REFERENCES energy_meter_data(id)
);

-- Opcional: Crear índices para optimizar consultas
CREATE INDEX idx_device_status_code ON device_status(code);
CREATE INDEX idx_wifi_status_sta_ip ON wifi_status(sta_ip);
CREATE INDEX idx_energy_meter_total ON energy_meter(total_act_power);