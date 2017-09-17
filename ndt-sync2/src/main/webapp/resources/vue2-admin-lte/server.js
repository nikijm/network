/*����express*/
var express = require('express');
var port = 8008;
var path = require('path');

var runPath = __dirname + '/dist'
/*ʵ����express*/
var app = express();
app.use(express.static(path.join(__dirname, 'dist')));
/*���ü����˿�,ͬʱ���ûص��������������¼�ʱִ�лص�����*/
app.get('*', function (req, res) {
  res.sendFile(path.join(__dirname, 'dist', 'index.html'));
});
app.listen(port, function afterListen () {
  console.log('---------------------------------------')
  console.log('express server root path:%s', runPath);
  console.log('express running on the http://localhost:%s', port);
});