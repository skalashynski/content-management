set root_folder_path=C://folder
set url=https://www.productiveedge.com
set driver_path=C://folder//chromedriver.exe
set page_scroll_value=600
set browser_name=chrome
set take_screenshot=true
set report_name=Report_name
set maximum_amount_internal_url_to_process=1
set allow_redirect=false
set generate_report=true
set start_process_from_an_index_page=true
set domain_name=www.productiveedge.com
set url_protocol=https
set url_port=80

java -jar content-mgmt-automation-jar-1.0-SNAPSHOT-jar-with-dependencies.jar ROOT_FOLDER_PATH=%root_folder_path% url=%url% domain_name=%domain_name% url_protocol=%url_protocol% url_port=%url_port% maximum_amount_internal_url_to_process=%maximum_amount_internal_url_to_process% allow_redirect=%allow_redirect% generate_report=%generate_report% report_name=%report_name% take_screenshot=%take_screenshot% browser_name=%browser_name% page_scroll_value=%page_scroll_value% driver_path=%driver_path%
