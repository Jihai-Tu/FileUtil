
# FileUtil
File Util that support import and export

## Support file format
Excel、Xls、Xlsx

## Support way
Local、Ftp、Sftp

# Download
Maven

```
<dependency>
	<groupId>org.projectlombok</groupId>
	<artifactId>lombok</artifactId>
	<version>${lombok.version}</version>
</dependency>
```
# How to user
## Export:
```
FileHandler fileHandler = null;
String basePath = baseParaService.getValue("account.service.file","basePath");
try {
    LocalFileRequest localFileRequest = new LocalFileRequest();
    localFileRequest.setBasePath(basePath);
    fileHandler = fileHandlerLoader.load(FilePathTypeEnum.FILE_PATH_TYPE_LOCAL, localFileRequest);
} catch (Exception e) {
    log.info("File Exception: failed to initialize file {}", FilePathTypeEnum.FILE_PATH_TYPE_LOCAL);
}

String filePath = baseParaService.getValue("account.service.file","uploadPath");
String backupFilePath = baseParaService.getValue("account.service.file","backup");
String fileName = baseParaService.getValue("account.service.file","export.name");
List<String> headerList = Arrays.asList("ID", "platform", "symbol", "price", "amount", "time", "type", "trade_id");
try {
    String tempFilePath = PoiUtils.writeXLSX(list,backupFilePath,fileName,Trade.class,headerList);
    InputStream inputStream = new FileInputStream(tempFilePath);
    fileHandler.write(filePath+fileName+".xlsx", inputStream);
} catch (Exception e) {
    log.error("File Exception: file export exception,{}", e);
    return;
}
```
## Import

```
String fileName = baseParaService.getValue("", "import.name");
FileHandler fileHandler = null;
try {
    LocalFileRequest fileRequest = new LocalFileRequest();
    fileHandler = fileHandlerLoader.load(FilePathTypeEnum.FILE_PATH_TYPE_SFTP,fileRequest);
} catch (Exception e) {
    log.info("File Exception: failed to initialize file {}", FilePathTypeEnum.FILE_PATH_TYPE_SFTP);
}
File file = fileHandler.read(fileName);
//backup
String backupFilePath = baseParaService.getValue("", "backup");
backupFilePath = backupFilePath.concat(DateUtils.currentDate());
try {
    FileUtils.copyFile(file, new File(backupFilePath + file.getName()));
} catch (IOException e) {
    log.error("File Exception: file backup failed,{}", e);
}

FileInputStream inputStream;
List<Trade> tradeList;
try {
    inputStream = new FileInputStream(file);
    //first sheet ,start with the second row
    tradeList = PoiUtils.readXLSX(inputStream, 0, 1, Trade.class);
} catch (FileNotFoundException e) {
    log.error("File Exception: file does not exist,{}", e);
    return;
} catch (IOException e) {
    log.error("File Exception: file parsing exception,{}", e);
    return;
}
```
# License


```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
