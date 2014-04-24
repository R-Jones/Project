/* <!--
  Licensed to the Apache Software Foundation (ASF) under one or more
  contributor license agreements.  See the NOTICE file distributed with
  this work for additional information regarding copyright ownership.
  The ASF licenses this file to You under the Apache License, Version 2.0
  (the "License"); you may not use this file except in compliance with
  the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
--> */
 /* <![CDATA[ */
        "use strict";

        var MessageType = Object.freeze({"Environment":1, "Private":2});
        
        var Chat = {};

        Chat.socket = null;

        Chat.connect = (function(host) {
            if ('WebSocket' in window) {
                Chat.socket = new WebSocket(host);
            } else if ('MozWebSocket' in window) {
                Chat.socket = new MozWebSocket(host);
            } else {
                Console.log('Error: WebSocket is not supported by this browser.');
                return;
            }

            Chat.socket.onopen = function () {
                Console.log('Connection opened.');
                document.getElementById('chat').onkeydown = function(event) {
                    if (event.keyCode == 13) {
                        Chat.sendMessage();
                    }
                };
            };

            Chat.socket.onclose = function () {
                document.getElementById('chat').onkeydown = null;
                Console.log('Connection lost');
            };

            Chat.socket.onmessage = function (message) {

                 var obj = JSON.parse(message.data);

                 if(obj.type === 1) {
                
                	 var playerList = document.getElementById('personlist');
             	
                	 while (playerList.hasChildNodes()) {
                		 playerList.removeChild(playerList.lastChild);
                	 }
                	 for (var i = 0;i < obj.pcList.length;i++) {
                		 var player = document.createElement('li');
                		 player.innerHTML = obj.pcList[i];
                		 playerList.appendChild(player);
                	 }
                
                	 var exitList = document.getElementById('exitlist');
               
                	 while (exitList.hasChildNodes()) {
                		 exitList.removeChild(exitList.lastChild);
                	 }
             	
                	 for (var j = 0;j < obj.exitList.length;j++) {
                		 var exit = document.createElement('li');
                		 exit.innerHTML = obj.exitList[j];
                		 exitList.appendChild(exit);
                	 }
                
                	 document.getElementById('roomdesc').innerHTML = obj.roomDesc;
                
                	 document.getElementById('roomname').innerHTML = obj.roomName;
                 }
                 else {
                     Console.log(obj.message);
                 }
            };
        });

        Chat.initialize = function() {
            if (window.location.protocol == 'http:') {
                Chat.connect('ws://' + window.location.host + '/Proj/socket');
            } else {
                Chat.connect('wss://' + window.location.host + '/Proj/socket');
            }
        };

        Chat.sendMessage = (function() {
            var message = document.getElementById('chat').value;
            if (message != '') {
                Chat.socket.send(message);
                document.getElementById('chat').value = '';
            }
        });

        var Console = {};

        Console.log = (function(message) {
            var console = document.getElementById('console');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.innerHTML = message;
            console.appendChild(p);
            while (console.childNodes.length > 100) {
                console.removeChild(console.firstChild);
            }
            console.scrollTop = console.scrollHeight;
        });

        Chat.initialize();


        document.addEventListener("DOMContentLoaded", function() {
            // Remove elements with "noscript" class - <noscript> is not allowed in XHTML
            var noscripts = document.getElementsByClassName("noscript");
            for (var i = 0; i < noscripts.length; i++) {
                noscripts[i].parentNode.removeChild(noscripts[i]);
            }
        }, false);
        

        $(function() {
            $( ".draggable" ).draggable();
          });

/* ]]> */