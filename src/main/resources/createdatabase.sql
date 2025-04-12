CREATE USER 'final'@'localhost' identified by 'final';
# 建立一個使用者帳號叫 final，密碼也是 final。
# @'localhost' 表示這個帳號 只能從本機登入（不能從外部 IP 連進來）。
# IDENTIFIED BY 'final' 是這個帳號的密碼
grant all privileges on *.* TO 'final'@'localhost';
# 給這個使用者 final 在所有資料庫（*.*）上的全部權限（ALL PRIVILEGES）。
# 包括查詢、插入、更新、刪除、建立資料表、建立資料庫... 等等。
# 注意：這幾乎等同於 root 權限（很高權限，要小心給）。