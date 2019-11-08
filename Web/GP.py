from http.server import BaseHTTPRequestHandler, HTTPServer
from hashlib import sha256
import cgi
import json

class GP(BaseHTTPRequestHandler):
    def _set_headers(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/html')
        self.end_headers()
    def do_HEAD(self):
        self._set_headers()
    def do_GET(self):
        html = ""
        path = self.path
        if path == "/":
            self._set_headers()
            index = open('index.html','r')
            html = index.read()
            index.close()
        elif path[:10]=="/messages/":
            pathList = path.split("/")
            if path[-1] == '/':
                self.send_error(404)
            else:
                ## Check if exist
                db = open('db.txt','r')
                lines = db.readlines()
                for i in lines:
                    print(pathList[2].encode('utf-8'),sha256(i[:-1].encode('utf-8')).hexdigest().encode('utf-8'))
                    if pathList[2].encode('utf-8') == sha256(i[:-1].encode('utf-8')).hexdigest().encode('utf-8'):
                        print("found it!!!")
                        html = i[:-1]
                db.close()
                ## Else return 404
                self._set_headers()
                # if len(pathList) > 2:
                #     html=sha256(pathList[2].encode('utf-8')).hexdigest()
                # else:
                #     html = path
        else:
            self._set_headers()
        bytes = html.encode(encoding='UTF-8')
        self.wfile.write(bytes)

    def do_POST(self):
        self._set_headers()
        content_len = int(self.headers.get('Content-Length'))
        post_body = self.rfile.read(content_len).decode('ascii')
        json1_data = json.loads(post_body)
        message = json1_data['message']
        hashStr = sha256(message.encode('utf-8')).hexdigest()
        db = open('db.txt','w')
        ##TODO(xiaolon): Write in new line
        db.write(message+'\n')
        db.close()
        html = '{"digest":'+hashStr+'}'
        bytes = html.encode(encoding='UTF-8')
        self.wfile.write(bytes)

def run(server_class=HTTPServer, handler_class=GP, port=8088):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print('Server running at localhost:8088...')
    httpd.serve_forever()

run()
