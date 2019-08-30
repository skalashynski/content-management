:: change paths according to requirements
set root_folder_path=.
set driver_path=%root_folder_path%\chromedriver.exe
set jar_name=content-mgmt-automation.jar
:: util's parameters

::starting page to execute
set process_url=https://www.productiveedge.com
set max_process_urls_value=5
set generate_report=true
:: don't forget to add the 'xlsx' extension to report name
set xlsx_report_name=today_report.xlsx
set report_sheet_name=report
:: true/false
set save_html=true
:: true/false
set save_txt=true
:: true/false
set take_screenshot=true
:: screen height
set page_screen_space_value=550


::dont't chanhe this parameters. They've not tested yet.
set browser_name=chrome
set process_strange_urls=false
set process_strategy=true

:: executable command. Don't change it.
java -jar %jar_name% root_folder_path=%root_folder_path% process_url=%process_url% process_strategy=%process_strategy% max_process_urls_value=%max_process_urls_value% generate_report=%generate_report% process_strange_urls=%process_strange_urls% xlsx_report_name=%xlsx_report_name% report_sheet_name=%report_sheet_name% save_html=%save_html% save_txt=%save_txt% take_screenshot=%take_screenshot% page_screen_space_value=%page_screen_space_value% browser_name=%browser_name% driver_path=%driver_path%
