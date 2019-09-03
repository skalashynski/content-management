#!/bin/bash

# change paths according to requirements
ROOT_FOLDER_PATH=/Users/susannagevorgyan/Documents/content-managment-automation
DRIVER_PATH=$ROOT_FOLDER_PATH/chromedriver
JAR_NAME=content-mgmt-automation.jar


# util's parameters

# starting page to execute
PROCESS_URL=https://www.humana.com/
MAX_PROCESS_URLS_VALUE=5
GENERATE_REPORT=true
# don't forget to add the 'xlsx' extension to report name
XLSX_REPORT_NAME=today_report.xlsx
REPORT_SHEET_NAME=report
# true/false
SAVE_HTML=true
# true/false
SAVE_TXT=true
# true/false
TAKE_SCREENSHOT=true
# screen height
PAGE_SCREEN_SPACE_VALUE=550


# dont't chanhe this parameters. They've not tested yet.
BROWSER_NAME=chrome
PROCESS_STRANGE_URLS=false
PROCESS_STRATEGY=true

# executable command. Don't change it.
java -jar \
$JAR_NAME root_folder_path=$ROOT_FOLDER_PATH process_url=$PROCESS_URL process_strategy=$PROCESS_STRATEGY \
max_process_urls_value=$MAX_PROCESS_URLS_VALUE generate_report=$GENERATE_REPORT \
process_strange_urls=$PROCESS_STRANGE_URLS xlsx_report_name=$XLSX_REPORT_NAME \
report_sheet_name=$REPORT_SHEET_NAME save_html=$SAVE_HTML save_txt=$SAVE_TXT \
take_screenshot=$TAKE_SCREENSHOT page_screen_space_value=$PAGE_SCREEN_SPACE_VALUE \
browser_name=$BROWSER_NAME driver_path=$DRIVER_PATH
