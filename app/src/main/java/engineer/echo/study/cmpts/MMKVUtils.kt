package engineer.echo.study.cmpts

import android.app.Application
import android.content.Context
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKV.SINGLE_PROCESS_MODE


class MMKVUtils {

    companion object {

        private const val TAG = "MMKVUtils"
        private const val TABLE_IMPORT = "is_import"
        private var sApp: Application? = null

        fun init(context: Application) {
            sApp = context
            MMKV.initialize(context)
        }


        /**
         * @param mode MMKV.MULTI_PROCESS_MODE 多进程共享
         */
        fun getSharedPreferences(
            tableName: String? = null,
            mode: Int = SINGLE_PROCESS_MODE,
            cryptKey: String? = null
        ): MMKV {
            return if (tableName == null) {
                MMKV.defaultMMKV()
            } else {
                // MultiProcessMode
                MMKV.mmkvWithID(tableName, mode, cryptKey)
            }
        }

        /**
         * 根据表名自动迁移源数据
         */
        private fun autoImport(tableName: String): MMKV {

            val preference = getSharedPreferences(tableName)
            if (!preference.decodeBool(TABLE_IMPORT) && sApp != null) {
                val oldTable = sApp!!.getSharedPreferences(tableName, Context.MODE_PRIVATE)
                getSharedPreferences(tableName).importFromSharedPreferences(oldTable)
                oldTable.edit().clear().apply()
                preference.encode(TABLE_IMPORT, true)
            }
            return preference
        }

        fun getSharedPreferencesBoolean(tableName: String, key: String, defValue: Boolean): Boolean {
            return autoImport(tableName).decodeBool(key, defValue)
        }

        fun getSharedPreferencesInt(tableName: String, key: String, defValue: Int = -1): Int {
            return autoImport(tableName).decodeInt(key, defValue)
        }

        fun getSharedPreferencesValue(tableName: String, key: String, defValue: Boolean): Boolean {
            return autoImport(tableName).decodeBool(key, defValue)
        }

        fun getSharedPreferencesValue(tableName: String, key: String, defValue: Float): Float {
            return autoImport(tableName).decodeFloat(key, defValue)
        }

        fun getSharedPreferencesValue(tableName: String, key: String, defValue: Int): Int {
            return autoImport(tableName).decodeInt(key, defValue)
        }

        fun getSharedPreferencesValue(tableName: String, key: String, defValue: Long): Long {
            return autoImport(tableName).decodeLong(key, defValue)
        }

        fun getSharedPreferencesValue(tableName: String, key: String, defValue: Double): Double {
            return autoImport(tableName).decodeDouble(key, defValue)
        }

        fun getSharedPreferencesValueNullable(tableName: String, key: String): String? {
            return autoImport(tableName).decodeString(key, null)
        }

        fun getSharedPreferencesValue(tableName: String, key: String, defValue: String): String {
            return autoImport(tableName).decodeString(key, defValue)
        }

        fun setSharedPreferences(tableName: String, key: String, value: Boolean) {
            getSharedPreferences(tableName).encode(key, value)
        }


        fun setSharedPreferences(tableName: String, key: String, value: Float) {
            getSharedPreferences(tableName).encode(key, value)
        }


        fun setSharedPreferences(tableName: String, key: String, value: Int) {
            getSharedPreferences(tableName).encode(key, value)
        }


        fun setSharedPreferences(tableName: String, key: String, value: Long) {
            getSharedPreferences(tableName).encode(key, value)
        }

        fun setSharedPreferences(tableName: String, key: String, value: Double) {
            getSharedPreferences(tableName).encode(key, value)
        }

        fun setSharedPreferences(tableName: String, key: String, value: String?) {
            getSharedPreferences(tableName).encode(key, value)
        }
    }

}