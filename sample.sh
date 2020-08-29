################################################################
# システム名      : 原材料受発注システム
# 機能名          : #shellId#_#shellName#
# 作成日          : 2020/08/15
# 最終更新日      : -
# 最終更新者      : NTTデータスミス
# 概要            : #shellName#
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
v_bat_id="#shellId#"
v_bat_name="#shellName#"
v_plpgsql_name="#shellId#"
#CSV_IMPORT_START
v_table_name="u_l_bl_tmp_shipment"
#CSV_IMPORT_END

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

#CSV_EXPORT_START
#ファイルエクスポートパラメーター声明
v_columnList=""
v_table_name="#tmp_table#"
v_order_col=""
v_output_path=""
v_format="csv"
v_delimiter=","
v_header="false"
v_quote="\""
v_escape="\\"
v_encoding="SJIS"
v_line_code=2
v_force_quote_flag="false"
#CSV_EXPORT_END

#CSV_IMPORT_START
#取り込みファイル名取得
v_value=`psql -h"${PGHOST}" -d"${PGDATABASE}" << EOF
SELECT family_get_name('${BCD}','B044','25','*','*','*','*','character_item_1')
EOF`
v_file_name=`echo $v_value | awk -F' ' '{print $3}'`
v_arr=(${v_value//' '/ })
if [[ ${#v_arr[*]} -lt 5 ]]; then
    v_file_name=""
fi

#コマンド
v_command="TRUNCATE TABLE ${v_table_name}"
#コマンドログの出力
MSG020=`GetMsg M0020 "${v_command}"`
PutLog "Info" "${v_bat_id}" "${v_bat_name}" "${MSG020}" "${v_log_file}"

#インポートテーブルをクリアする
psql -h"${PGHOST}" -d"${PGDATABASE}" << EOF
TRUNCATE TABLE ${v_table_name}
EOF

#取り込み処理実行
sh ${BATCH_COMMON_PATH}/file_import.sh ${v_table_name} ${v_file_name}  "csv" "," "false" "\""  "\\" ${ENCODING} ${v_bat_id} ${v_bat_name}
v_rc=$?
if [[ $v_rc -eq 0 ]]; then
#CSV_IMPORT_END

#プロシージャ開始ログの出力
MSG015=`GetMsg M0015 "${v_plpgsql_name}"`
PutLog "Info" "${v_bat_id}" "${v_bat_name}" "${MSG015}" "${v_log_file}"

#プロシージャ呼び出し
v_value=`psql -h"${PGHOST}" -d"${PGDATABASE}" << EOF
select #shellId#('${BCD}')
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
#CSV_IMPORT_START
fi
#CSV_IMPORT_END

#CSV_EXPORT_START
#CSVファイルエクスポート
if [[ $v_rc -lt 90 ]]; then

#バス取得
v_value=`psql -h"${PGHOST}" -d"${PGDATABASE}" << EOF
SELECT family_get_name('${BCD}','B045','45','*','*','*','*','character_item_1')
EOF`
v_output_path=`echo $v_value | awk -F' ' '{print $3}'`

    v_arr=(${v_value//' '/ })
    if [[ ${#v_arr[*]} -lt 5 ]]; then
        v_output_path=""
    fi

    if [[ $v_output_path != "" ]]; then
                #出力処理
        sh ${BATCH_COMMON_PATH}/file_export.sh ${v_columnList} ${v_table_name} ${v_order_col} ${v_output_path} ${v_format} ${v_delimiter} ${v_header} ${v_quote} ${v_escape} ${v_encoding} ${v_line_code} ${v_bat_id} ${v_bat_name} ${v_force_quote_flag}
        v_rc=$?

        #処理結果ログ       
        if [[ $v_rc -lt 90 ]]; then
            # #origin_table_name#更新
            psql -h"${PGHOST}" -d"${PGDATABASE}" << EOF

EOF
        else 
            # #origin_table_name#更新
            psql -h"${PGHOST}" -d"${PGDATABASE}" << EOF

EOF
        fi
    else
        #出力処理開始
        MSG010=`GetMsg M0010 ${v_plpgsql_name}`
        PutLog "Info" "${v_bat_id}" "${v_bat_name}" "${MSG010}" "${v_log_file}"

        #出力処理異常終了
        MSG012=`GetMsg M0012 ${v_plpgsql_name}`
        PutLog "Info" "${v_bat_id}" "${v_bat_name}" "${MSG012}" "${v_log_file}"

        # #origin_table_name#連携日時更新
        psql -h"${PGHOST}" -d"${PGDATABASE}" << EOF

EOF
    fi
fi
#CSV_EXPORT_END

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