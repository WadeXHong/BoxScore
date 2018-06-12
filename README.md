# BoxScore ( 球經救星 )
An application for basketball teams to easily record their game data.<br />
球經救星是一款讓球隊可以輕鬆利用手機優雅紀錄球賽數據的 App，<br />
讓你不需要在畫正字記號與塗塗改改之間失望落寞， <br />
讓我們放下紙跟筆，say sure that's go！<br />

[<img src="https://github.com/WadeXHong/BoxScore/blob/master/ScreenShot/google_player_icon.png" width="200" height="77.5">](https://play.google.com/store/apps/details?id=com.wadexhong.boxscore)

# Featrue
* 主選單
  * 以 email 註冊、登入 Firebase。
 
* 開始遊戲
  * 詢問是否恢復中斷的比賽。
  * 顏色變化提示輸入是否合法。
  * 輸入若不符規定，中斷單向 ViewPager 滑動、TabLayout切換。
  * 所屬球隊連動改變球員名單。

* 比賽紀錄畫面
  * 透過多點手勢簡化純按鈕點擊操作。
  * 可自定義手勢對應記錄內容。
  * 以震動回饋使用者觸發事件。
  * 儲存操作紀錄並提供刪除，修改功能。
  * 更換場上球員功能。
  * 即時數據統計查看。
  * 返回鍵防呆、同步儲存確認、中斷恢復機制。
  
* 歷史紀錄
  * 比賽列表。
  * 簡易統計圖表。
  * 團隊數據總和。
  * 個人數據總表、時間篩選。
  
* 個人設定
  * 亮度調整。
  * 手勢設定。
  * 儲存設定值。
  * 登出。

# Library
* Firebase
* MPAndroidChart
* AdaptiveTableLayout
* PermissionsDispatcher


# Screenshot
<img src="https://github.com/WadeXHong/BoxScore/blob/master/ScreenShot/login_page.jpg" width="180" height="320">  <img src="https://github.com/WadeXHong/BoxScore/blob/master/ScreenShot/team_manage.jpg" width="180" height="320">  <img src="https://github.com/WadeXHong/BoxScore/blob/master/ScreenShot/team_player.jpg" width="180" height="320">  <img src="https://github.com/WadeXHong/BoxScore/blob/master/ScreenShot/playerlist_setting.jpg" width="180" height="320"></br>
<img src="https://github.com/WadeXHong/BoxScore/blob/master/ScreenShot/record_page.jpg" width="180" height="320">  <img src="https://github.com/WadeXHong/BoxScore/blob/master/ScreenShot/gesture.gif" width="180" height="320">  <img src="https://github.com/WadeXHong/BoxScore/blob/master/ScreenShot/undohistory.gif" width="180" height="320">  <img src="https://github.com/WadeXHong/BoxScore/blob/master/ScreenShot/gamehistory.gif" width="180" height="320"></br>

# Requirement
* Android 5.1+

# Version
* 1.0.4 - 2018/06/06
  * 刪除球員功能。

* 1.0.3 - 2018/06/06
  * 新增修改紀錄功能。
 
* 1.0.2 - 2018/06/02
  * 改為FireBase登入制，可處存資料至雲端，增加點擊震動並修正多項bug。
 
* 1.0.1 - 2018/05/28
  * 修正預設球員名單進入比賽crash問題，行距對齊等。
 
* 1.0.0 - 2018/05/27
  * 基本球隊、球員、比賽紀錄、歷史回顧功能。

# Contacts
有發現任何 bug，或是有任何使用者體驗優化建議、新功能期許或 idea，都歡迎透過下方資訊聯絡我！

Wade Hong <br />
wadexhong@gmail.com
