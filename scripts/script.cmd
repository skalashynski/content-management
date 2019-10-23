:: change paths according to requirements
set root_folder_path=C://folder
set driver_path=C://folder//chromedriver.exe
set jar_name=content-mgmt-automation.jar
:: util's parameters

::starting page to execute
set page_domain_url=https://www.workfusion.com/careers/
set max_process_urls_value=200
set generate_links_per_page_report=true

:: true/false
set save_html=true
:: true/false
set save_txt=true
:: true/false
set take_screenshot=false
:: screen height
set page_screen_space_value=550

set tags_to_analyze=all

set browser_name=chrome
set process_strange_urls=false

:: executable command. Don't change it.
java -jar %jar_name% root_folder_path=%root_folder_path% page_domain_url=%page_domain_url% max_process_urls_value=%max_process_urls_value% generate_links_per_page_report=%generate_links_per_page_report% process_strange_urls=%process_strange_urls% save_html=%save_html% save_txt=%save_txt% take_screenshot=%take_screenshot% page_screen_space_value=%page_screen_space_value% browser_name=%browser_name% driver_path=%driver_path% tags_to_analyze=%tags_to_analyze%
