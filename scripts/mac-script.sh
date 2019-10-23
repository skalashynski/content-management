#!/bin/bash

# change paths according to requirements
ROOT_FOLDER_PATH=/Users/susannagevorgyan/Documents/content-managment-automation
DRIVER_PATH=$ROOT_FOLDER_PATH/chromedriver
JAR_NAME=content-mgmt-automation.jar


# util's parameters

# starting page to execute
PAGE_DOMAIN_URL=https://www.humana.com/
MAX_PROCESS_URLS_VALUE=5
GENERATE_LINKS_PER_PAGE_REPORT=true

# true/false
SAVE_HTML=true
# true/false
SAVE_TXT=true
# true/false
TAKE_SCREENSHOT=false
# screen height
PAGE_SCREEN_SPACE_VALUE=550

TAGS_TO_ANALYZE=ALL


# dont't change this parameters. They've not tested yet.
BROWSER_NAME=chrome
PROCESS_STRANGE_URLS=false

# executable command. Don't change it.
java -jar $JAR_NAME \
root_folder_path=$ROOT_FOLDER_PATH \
page_domain_url=$PAGE_DOMAIN_URL \
max_process_urls_value=$MAX_PROCESS_URLS_VALUE \
GENERATE_LINKS_PER_PAGE_REPORT=$GENERATE_LINKS_PER_PAGE_REPORT \
process_strange_urls=$PROCESS_STRANGE_URLS \
xlsx_report_name=$XLSX_REPORT_NAME \
report_sheet_name=$REPORT_SHEET_NAME \
save_html=$SAVE_HTML save_txt=$SAVE_TXT \
take_screenshot=$TAKE_SCREENSHOT \
page_screen_space_value=$PAGE_SCREEN_SPACE_VALUE \
browser_name=$BROWSER_NAME \
driver_path=$DRIVER_PATH \
tags_to_analyze=$TAGS_TO_ANALYZE
