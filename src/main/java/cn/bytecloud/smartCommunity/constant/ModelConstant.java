package cn.bytecloud.smartCommunity.constant;

public class ModelConstant {
    public static final String CREATE_TIME = "l_create_time";
    public static final String UPDATE_TIME = "l_update_time";
    public static final String CREATOR_ID = "s_creator_id";
    public static final String DESC = "s_desc";
    public static final String ID = "_id";

    /**
     * user table
     */
    public static final String T_USER = "t_user";
    public static final String USER_USERNAME = "s_username";
    public static final String USER_NAME = "s_name";
    public static final String USER_IMAGE_PATH = "s_image_path";
    public static final String USER_PASSWORD = "s_password";
    public static final String USER_USER_FLAG = "b_user_flag";
    public static final String USER_TELEPHONE = "s_telephone";
    public static final String USER_AGE = "s_age";
    public static final String USER_GENDER = "s_gender";
    public static final String USER_BIRTHDAY = "s_birthday";
    public static final String USER_EMAIL = "s_email";
    public static final String USER_ADDRESS = "s_address";
    public static final String USER_UNIT_ID = "s_unit_id";
    public static final String USER_ROLE_IDS = "a_role_ids";
    public static final String USER_TYPE = "s_type";
    public static final String USER_NUM = "i_num";
    public static final String USER_SOUND_FLAG = "b_sound_flag";


    /**
     * node table
     */
    public static final String T_NODE = "t_node";
    public static final String NODE_NAME = "s_name";
    public static final String NODE_WEB_ID = "s_web_id";
    public static final String NODE_TYPE = "s_type";
    public static final String NODE_ATTRIBUTE = "s_attribute";
    public static final String NODE_BUTTONS = "a_buttons";
    public static final String NODE_UPLOAD_FLAG = "b_upload_flag";
    public static final String NODE_PROCESS_ID = "s_process_id";
    public static final String NODE_HANDLER_TYPE = "s_handler_type";
    public static final String NODE_UNIT_ID = "s_unit_id";



    /**
     * process table
     */
    public static final String T_PROCESS = "t_process";
    public static final String PROCESS_STYLE = "s_style";
    public static final String PROCESS_NAME = "s_name";
    public static final String PROCESS_SOURCE = "s_source";
    public static final String PROCESS_VERSION = "i_version";



    public static final String T_ADDRESS = "t_address";
    public static final String ADDRESS_LATITUDE = "t_latitede";
    public static final String ADDRESS_NUM = "i_num";
    public static final String ADDRESS_LONGITUDE = "t_longitude";
    public static final String ADDRESS_UNIT = "t_unit_id";


    /**
     * path table
     */
    public static final String T_PATH = "t_path";
    public static final String PATH_NAME = "s_name";
    public static final String PATH_TYPE = "s_type";
    public static final String PATH_ATTRIBUTE = "s_attribute";
    public static final String PATH_TIME = "l_time";
    public static final String PATH_START_NODE_ID = "s_start_node_id";
    public static final String PATH_END_NODE_ID = "s_end_node_id";
    public static final String PATH_PROCESS_ID = "s_process_id";
    public static final String PATH_WEB_ID = "s_web_id";

    /**
     * table work
     */
    public static final String T_WORK = "t_work";
    public static final String WORK_TITLE = "s_title";
    public static final String WORK_COUNT = "i_count";
    public static final String WORK_UNIT_ID = "s_unit_id";
    public static final String WORK_RETURN_FLAG = "b_return_flag";
    public static final String WORK_DELAY_FLAG = "b_delay_flag";
    public static final String WORK_TYPE_ID = "s_type_id";
    public static final String WORK_WORK_TYPE = "s_work_type";
    public static final String WORK_ACCEPT_FLAG = "s_review_flag";
    public static final String WORK_NUM = "s_num";
    public static final String WORK_PROCESS_ID = "s_process_id";
    public static final String WORK_END_FLAG = "b_end_flag";
    public static final String WORK_LATITUDE = "s_latitude";
    public static final String WORK_LONGITUDE = "s_longitude";
    public static final String WORK_ADDRESS = "s_address";
    public static final String WORK_SOURCE = "s_source";
    public static final String WORK_RETURN_USER_IDS = "a_return_user_ids";
    public static final String WORK_CURR_NODE_ID = "s_curr_node_id";
    public static final String WORK_BEFORE_PATH_ID = "s_before_path_id";
    public static final String WORK_HANDLE_BEFORE_IMAGE_PATHS = "a_handle_before_image_paths";
    public static final String WORK_HANDLE_BEFORE_VIDEO_PATHS = "a_handle_before_video_paths";
    public static final String WORK_HANDLE_AFTER_IMAGE_PATHS = "a_handle_after_image_paths";
    public static final String WORK_HANDLE_AFTER_VIDEO_PATHS = "a_handle_after_video_paths";
    public static final String WORK_HANDLE_DESC = "s_handle_desc";
    public static final String WORK_END_TIME = "s_end_time";
    public static final String WORK_FINISH_TIME = "s_finish_time";
    public static final String WORK_FINISH_USER_ID = "s_finish_user_id";
    public static final String WORK_BLACKLIST_ID = "s_blacklist_id";
    public static final String WORK_DEVICE_ID = "s_device_id";
    public static final String WORK_READER_IDS = "a_reader_ids";
    public static final String WORK_STATUS = "s_status";
    public static final String WORK_CREATE_YEAR = "i_create_year";
    public static final String WORK_FINISH_YEAR = "i_finish_year";
    public static final String WORK_CREATE_MONTH = "i_create_moth";
    public static final String WORK_FINISH_MONTH = "i_finish_month";
    public static final String WORK_FINISH_DAY = "i_finish_day";
    public static final String WORK_CREATE_DAY = "i_create_day";
    public static final String WORK_EVENT_ID = "s_event_id";


    /**
     * 代办 table
     */
    public static final String T_TODO = "t_todo";
    public static final String TODO_TYPE = "s_type";
    public static final String TODO_WORK_TYPE = "s_work_type";
    public static final String TODO_WORK_ID = "s_work_id";
    public static final String TODO_TITLE = "s_title";
    public static final String TODO_NUM = "s_num";
    public static final String TODO_ADDRESS = "s_address";
    public static final String TODO_WORK_SOURCE = "s_work_source";
    public static final String TODO_BEFORE_ATTRIBUTE = "s_before_attribute";
    public static final String TODO_WORK_TYPE_ID = "s_work_type_id";
    public static final String TODO_CURR_NODE_ID = "s_curr_node_id";
    public static final String TODO_BEFORE_PATH_ID = "s_before_path_id";
    public static final String TODO_HANDLER_IDS = "a_handlerIds";
    public static final String TODO_END_TIME = "l_end_time";
    public static final String TODO_TIME_TYPE = "s_time_type";
    public static final String TODO_TIME = "l_time";


    /**
     * finish table
     */
    public static final String T_FINISH = "t_finish";
    public static final String FINISH_WORK_ID = "s_work_id";
    public static final String FINISH_BEFORE_PATH_ID = "s_before_path_id";
    public static final String FINISH_HANDLER_IDS = "a_handlerIds";
    public static final String FINISH_BEFORE_ATTRIBUTE = "s_before_attribute";
    public static final String FINISH_AFTER_ATTRIBUTE = "s_after_attribute";
    public static final String FINISH_AFTER_PATH_ID = "s_after_path_id";
    public static final String FINISH_AFTER_NODE_ID = "s_after_node_id";
    public static final String FINISH_HANDLER_NODE_ID = "s_handler_node_id";
    public static final String FINISH_LOG_ID = "s_log_id";
    public static final String FINISH_TITLE = "s_title";
    public static final String FINISH_TYPE = "s_type";
    public static final String FINISH_END_TIME = "l_ent_time";
    public static final String FINISH_WORK_TYPE = "s_work_type";
    public static final String FINISH_NUM = "s_num";
    public static final String FINISH_ADDRESS = "s_address";
    public static final String FINISH_CURR_NODE_ID = "s_curr_node_id";
    public static final String FINISH_SOURCE = "s_source";
    public static final String FINISH_WORK_SOURCE = "s_work_source";
    public static final String FINISH_WORK_TYPE_ID = "s_work_type_id";
    public static final String FINISH_TIME = "l_time";
    public static final String FINISH_TIME_TYPE = "s_time_type";


    /**
     * log table
     */
    public static final String T_LOG = "t_log";
    public static final String LOG_WORK_ID = "s_work_id";
    public static final String LOG_HANDLER_COUNT = "l_handler_count";
    public static final String LOG_UNIT_ID = "s_unit_id";
    public static final String LOG_END_FLAG = "b_end_flag";
    public static final String LOG_HANDLER_TIME = "l_handler_time";

    public static final String LOG_ITEM = "a_log_item";
    public static final String LOG_ITEM_PATH_ID = "s_path_id";
    public static final String LOG_ITEM_TYPE = "s_type";
    public static final String LOG_ITEM_TODO_ID = "s_todo_id";
    public static final String LOG_ITEM_TIME = "l_time";
    public static final String LOG_ITEM_TIME_TYPE = "s_time_type";
    public static final String LOG_ITEM_USER_IDS= "a_user_ids";
    public static final String LOG_ITEM_TODO_IDS= "a_todo_ids";

    /**
     * unit table
     */
    public static final String T_UNIT = "t_unit";
    public static final String UNIT_NAME = "s_name";
    public static final String UNIT_NUM = "i_num";
    public static final String UNIT_ABBRE = "s_abbre";
    public static final String UNIT_ADDRESS = "s_address";
    public static final String UNIT_TELEPHONE = "s_telephone";
    public static final String UNIT_PID = "s_pid";


    /**
     * permission table
     */
    public static final String T_PERMISSION = "t_permission";
    public static final String PERMISSION_NAME = "s_name";
    public static final String PERMISSION_URL = "s_interface_url";
    public static final String PERMISSION_MENU_ID = "s_menu_id";


    /**
     * role table
     */
    public static final String T_ROLE = "t_role";
    public static final String ROLE_NAME = "s_name";
    public static final String ROLE_NUM = "i_num";
    public static final String ROLE_UNIT_ID = "s_unit_id";
    public static final String ROLE_DEVICE_IDS = "a_device_ids";
    public static final String ROLE_PERMISSIONS = "a_permissions";


    /**
     * menu table
     */
    public static final String T_MENU = "t_menu";
    public static final String MENU_NAME = "s_name";
    public static final String MENU_PID = "s_pid";

    /**
     * bigCategory table
     */
    public static final String T_BIG_CATEGORY = "t_big_category";
    public static final String BIG_CATEGORY_NAME = "s_name";
    public static final String BIG_CATEGORY_NUM = "i_num";
    public static final String BIG_CATEGORY_ABBRE = "s_abbre";


    /**
     * smallCategory table
     */
    public static final String T_SMALL_CATEORY = "t_small_category";
    public static final String SMALL_CATEORY_NAME = "s_name";
    public static final String SMALL_CATEORY_ABBRE = "s_abbre";
    public static final String SMALL_CATEORY_UNIT_ID = "s_unit_id";
    public static final String SMALL_CATEORY_BIG_ID = "s_big_id";
    public static final String SMALL_CATEORY_TIME = "L_time";
    public static final String SMALL_CATEORY_TIME_TYPE = "s_time_type";
    public static final String SMALL_CATEORY_TYPE = "s_type";


    /**
     * station table
     */
    public static final String T_STATION = "t_station";
    public static final String STATION_NAME = "s_name";
    public static final String STATION_ADDRESS = "s_address";
    public static final String STATION_LATITUDE = "s_latitude";
    public static final String STATION_LONGITUDE = "s_longitude";


    /**
     * blacklist table
     */
    public static final String T_BLACKLIST = "t_blacklist";
    public static final String BLACKLIST_NAME = "s_name";
    public static final String BLACKLIST_TYPE_ID = "s_type_id";
    public static final String BLACKLIST_LEFT_IMAGE_PATH = "s_left_image_path";
    public static final String BLACKLIST_RIGHT_IMAGE_PATH = "s_right_image_path";
    public static final String BLACKLIST_IMAGE_PATH = "s_image_path";
    public static final String BLACKLIST_CODE = "s_code";

    /**
     * device table
     */
    public static final String T_DEVICE = "t_device";
    public static final String DEVICE_NUM = "s_num";
    public static final String DEVICE_NAME = "s_name";
    public static final String DEVICE_VENDOR = "s_vendor";
    public static final String DEVICE_ADDRESS = "s_address";
    public static final String DEVICE_LATITUDE = "s_latitude";
    public static final String DEVICE_LONGITUDE = "s_longitude";
    public static final String DEVICE_UNIT_ID = "s_unit_id";
    public static final String DEVICE_TYPE = "s_type";
    public static final String DEVICE_FEATURES = "s_features";
    public static final String DEVICE_IP = "s_ip";
    public static final String DEVICE_TCP_PORT = "i_tcp_port";
    public static final String DEVICE_STATUS = "s_status";
    public static final String DEVICE_RTSP_ADDRESS = "s_rtsp_address";
    public static final String DEVICE_RTSP_PORT = "s_rtsp_port";
    public static final String DEVICE_USERNAME = "s_username";
    public static final String DEVICE_PASSWORD = "s_password";


    /**
     * basis table
     */
    public static final String T_BASIS = "t_basis";
    public static final String BASIS_NAME = "s_name";
    public static final String BASIS_IMAGE_PATH = "s_path";
    public static final String BASIS_FILE_MAX_SIZE = "l_file_max_size";
    public static final String BASIS_TIME_OUT = "l_time_out";
    public static final String BASIS_LATITUDE = "s_latitude";
    public static final String BASIS_LONGITUDE = "s_longitude";
    public static final String BASIS_SOUND_NUM = "i_sound_num";
    public static final String BASIS_SOUND_FLAG = "b_sound_flag";

    /**
     * record table
     */
    public static final String T_RECORD = "t_record";
    public static final String RECORD_WORK_ID = "s_work_id";
    public static final String RECORD_TIME = "l_time";
    public static final String RECORD_DATA = "a_data";
    public static final String RECORD_TYPE = "s_type";
    public static final String RECORD_TODO_ID = "s_todo_id";


    /**
     * useraddress table
     */
    public static final String T_USER_ADDRESS = "t_user_address";
    public static final String USER_ADDRESS_LATITUDE = "d_latitude";
    public static final String USER_ADDRESS_LONGITUDE = "d_longitude";
    public static final String USER_ADDRESS_ONLINE_FLAG = "b_online_flag";


    /**
     * useraddress table
     */
    public static final String T_VERSION = "t_version";
    public static final String VERSION_TYPE = "s_type";
    public static final String VERSION_URL = "s_url";
    public static final String VERSION_NUM = "s_num";

    /**
     * STATS table
     */
    public static final String T_STATS = "t_stats";
    public static final String STATS_YEAR = "i_year";
    public static final String STATS_MONTH = "i_month";
    public static final String STATS_DAY = "i_day";
    public static final String STATS_USER_ONLINE = "d_user_onlien";
    public static final String STATS_UNIT_ID = "s_unit_id";

    /**
     * STATS table
     */
    public static final String T_FENCE = "t_fence";
    public static final String FENCE_NAME = "s_name";
    public static final String FENCE_NUM = "a_num";
    public static final String FENCE_TYPE = "a_type";
    public static final String FENCE_DEVICE_ID = "s_device_id";

}


