################################################################
# システム名      : 原材料受発注システム
# 機能名          : NJ_B_DB0110_店舗発注データインポート
# 作成日          : 2020/08/15
# 最終更新日      : -
# 最終更新者      : NTTデータスミス
# 概要            : 店舗発注データインポート
# 引数            : なし
# 正常終了        : [0]正常終了
# 異常終了        : [90]システムエラー　[91]データ無エラー
################################################################

#共通設定定義を読み込み（パスは変更予定）
source /mnt/efs/job/batch/bin/conf/common.conf
source ${ENV_COMMON_FNC}
export PGUSER
export PGPASSWORD

#変数定義
#プログラムファイルの指定
v_bat_id="NJ_B_DB0110"
v_bat_name="店舗発注データインポート"
v_plpgsql_name="NJ_B_DB0110"

#バッチログファイル定義
v_log_file=${ENV_BATCH_LOG}
#リターンコードの初期化
v_rc=0

#処理正常終了
INFO_MSG=`GetMsg M0002`
#処理異常終了
ERROR_MSG=`GetMsg M0003`

#開始ログの出力
MSG001=`GetMsg M0001`
PutLog "Info" "${v_bat_id}" "${v_bat_name}" "${MSG001}" "${v_log_file}"



#プロシージャ開始ログの出力
MSG015=`GetMsg M0015 "${v_plpgsql_name}"`
PutLog "Info" "${v_bat_id}" "${v_bat_name}" "${MSG015}" "${v_log_file}"

#プロシージャ呼び出し
v_value=`psql -h"${PGHOST}" -d"${PGDATABASE}" << EOF
select NJ_B_DB0110('${BCD}')
EOF`

v_rc=`echo $v_value | awk -F' ' '{print $3}'`

     #処理結果ログ
    if [[ $v_rc -lt 90 ]]; then
       #プロシージャ処理正常終了ログの出力
       MSG016=`GetMsg M0016 ${v_plpgsql_name}`
       PutLog "Info" "${v_bat_id}" "${v_bat_name}" "${MSG016}" "${v_log_file}"
    else 
       #プロシージャ処理異常終了ログの出力
       MSG017=`GetMsg M0017 ${v_plpgsql_name}`
       PutLog "Error" "${v_bat_id}" "${v_bat_name}" "${MSG017}" "${v_log_file}"
    fi


#処理結果ログ
if [[ $v_rc -lt 90 ]]; then
     #処理正常終了
    END_MSG=`GetMsg M0002`
    END_LOG_LEVEL="Info"
else 
     #処理異常終了
    END_MSG=`GetMsg M0003`
    END_LOG_LEVEL="Error"
fi

#返却値ログ
RETURN_MSG=`GetMsg M0021 "${v_rc}"`
PutLog ${END_LOG_LEVEL} "${v_bat_id}" "${v_bat_name}" "${RETURN_MSG}" "${v_log_file}"
#処理終了ログ
PutLog ${END_LOG_LEVEL} "${v_bat_id}" "${v_bat_name}" "${END_MSG}" "${v_log_file}"

#終了コードの返却
exit $v_rc

