# BoxScore
An Application for basketball teams to easilly record their game data.

# 球經救星 
球經救星是一款讓球隊可以透過手機紀錄球賽數據的 App <br />
讓你不需要在畫正字記號與漏記之間失望落寞， <br />
讓我們放下紙跟筆，say sure that's go！<br />

[<img src="https://github.com/WadeXHong/BoxScore/blob/master/ScreenShot/google_player_icon.png" width="150" height="50">](https://play.google.com/store/apps/details?id=com.wadexhong.boxscore)

# Featrue
* 主選單
 * 以 email 註冊、登入。
 * 點擊球隊管理新增球隊和至少六名球員。
 * 點擊開始比賽，依照提示輸入對手名稱、建置上場球員名單等內容，即可開始比賽。
 * 歷史紀錄供使用者查閱已儲存並結束的比賽統計內容。
 * 個人設定可調節螢幕亮度、設定操作手勢以及登出。
 
* 開始遊戲
 * 查看是否有未結束比賽，可進行復原或刪除。
 * 名稱設定必填選項為對手名稱及所屬球隊；日期由月曆選取，預設為當日。
 * 球員名單顯示所屬球隊的所有球員，先發名單必須為 5 人、替補球員接受範圍為 1 ~ 10 人。
 * 操作設定可藉由調整鈕自由調整。

* 比賽紀錄畫面
 * 右上角齒輪圖示可進入個人設定頁面，調整亮度及手勢。
 * 透過直接點擊數據紀錄內的按鈕或是操作手勢，依照視窗選取紀錄對象。
 * 對方團隊數據以及節數，可藉由直接點選數字或是旁邊的操作按鈕調整。
 * 我方團隊數據將依據球員的個人數據紀錄改變。
 * 點選操作紀錄內容可操作刪除及變更選項，修正錯誤紀錄內容。
 * 更換球員藉由點擊任意球員，進行一對一球員交換。
 * 數據統計按鈕顯示目前的詳細記錄內容。
 * 返回鍵防呆選項視窗及非預期關閉遊戲皆可保留目前記錄狀態。
 * 左上角儲存鍵結束並同步完整比賽至Firebase。


# Library
* Firebase
* MPAndroidChart
* AdaptiveTableLayout
* PermissionsDispatcher


# Screenshot
<img src="https://github.com/WadeXHong/BoxScore/blob/master/ScreenShot/login_page.jpg" width="270" height="480"> 
<img src="https://github.com/WadeXHong/BoxScore/blob/master/ScreenShot/gesture.gif" width="270" height="480"> 

# Requirement
* Android 5.1+

# Version
* 1.0.4 - 2018/06/06
 刪除球員功能。

* 1.0.3 - 2018/06/06
 新增修改紀錄功能。
 
* 1.0.2 - 2018/06/02
 改為FireBase登入制，可處存資料至雲端，增加點擊震動並修正多項bug。
 
* 1.0.1 - 2018/05/28
 修正預設球員名單進入比賽crash問題，行距對齊等。
 
* 1.0.0 - 2018/05/27
 基本球隊、球員、比賽紀錄、歷史回顧功能。

# Contacts
洪偉軒 <br />
wadexhong@gmail.com
