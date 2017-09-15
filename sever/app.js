const express = require('express');
const app = express();
const server = require('http').Server(app);
const io = require('socket.io')(server);
const socketio = require('./socketio')(io);
const OpenTok = require('opentok');

const API_KEY = '45956472';
const API_SECRET = '81b2bef88a6c486fbcc6036d40b3319aa4bdbc1c';

// get initial session id 
var opentok = new OpenTok(API_KEY, API_SECRET);
// var opentok = new OpenTok.OpenTokSDK(API_KEY, API_SECRET);

var sessionId = [];
var m = 0,
    n = 0;
var member = [];
var cvt = [];

Array.prototype.contains = function(data) {
    var i = this.length;
    while (i--) {
        if (this[i].id === data.id) {
            return true;
        }
    }
    return false;
}

Array.prototype.push = function(obj) {
    this[this.length++] = obj;
}

Array.prototype.get = function(i) {
    return this[i];
}

function scramble(array) {
    var tmp, current, top = array.length;

    if (top)
        while (--top) {
            current = Math.floor(Math.random() * (top + 1));
            tmp = array[current];
            array[current] = array[top];
            array[top] = tmp;
        }
    return array;
}

member = scramble(member);
cvt = scramble(cvt);

server.listen(3000, () => {
    console.log(`Server is running at localhost:3000`);
});

io.on('connection', function(socket) {
    // console.log('A user connected ');

    socket.on('test', function(data) {
        console.log(data);
    });

    socket.on('get-session-id', function(data) {
        {
            //==== storage data from client
            console.log(data);
            if (data.role != "Member") {
                if (!member.contains(data)) {
                    var mIndex = member.length;
                    member.push({ id: data.fbId, hasSession: "false", sesionId: "", socket: socket });
                    console.log("Member: " + member.length);
                    var ok = false;
                    var check = setInterval(function() {
                        if (member[mIndex].hasSession === "true") {
                            if (ok === true) {
                                return;
                            }
                            console.log("Session ID sending to member " + (mIndex + 1) + ":\n" + member[mIndex].sessionId);
                            socket.emit('return-session-id', { sessionId: member[mIndex].sessionId, token: opentok.generateToken(member[mIndex].sessionId) });
                            ok = true;
                        }
                    }, 1000);
                } else {
                    console.log("This user is existed");
                }
            } else if (!cvt.contains(data)) {
                var cIndex = cvt.length;
                cvt.push({ id: data.fbId, hasSession: "false", sesionId: "", socket: socket });
                console.log("CTV: " + cvt.length);
                var ok = false;
                var check = setInterval(function() {
                    if (cvt[cIndex].hasSession === "true") {
                        if (ok === true) {
                            return;
                        }
                        console.log("Session ID sending to cvt " + (cIndex + 1) + ":\n" + cvt[cIndex].sessionId);
                        socket.emit('return-session-id', { sessionId: cvt[cIndex].sessionId, token: opentok.generateToken(cvt[cIndex].sessionId) });
                        ok = true;
                    }
                }, 1000);
                if (cvt.length % 2 == 0) {
                    if (cvt.length / 2 > sessionId.length) {
                        createNewSession();
                    }
                }
            } else {
                console.log("This user is existed");
            }
            socket.on('disconnect', function() {
                console.log('user disconnected');
            });
        }
    });

    socket.on('start-call', function(data) {
        start();
    });
});

function createNewSession() {

    opentok.createSession({ mediaMode: "routed" }, function(error, session) {
        if (error) {
            console.log("Error creating session:\n", error)
        } else {
            sessionId[sessionId.length] = session.sessionId;
            console.log("Session ID: " + sessionId[sessionId.length - 1]);
            if (sessionId.length > 1) start();
        }
    });

}
createNewSession();

// Start connecting users
function start() {
    console.log("Starting\nNumer of session: " + sessionId.length);
    scramble(member);
    scramble(cvt);
    n = member.length;
    m = cvt.length;
    console.log("n: " + n);
    console.log("m:" + m);
    var numberOfSession = Math.max(n, Math.trunc(m / 2))
    console.log("max: " + numberOfSession);
    for (var index = 0; index < sessionId.length; index++) {
        if (index < n) {
            member[index].sessionId = sessionId[index];
            member[index].hasSession = "true";
        }
        if (index * 2 < m) {
            cvt[index * 2].sessionId = sessionId[index];
            cvt[index * 2].hasSession = "true";
        }
        if (index * 2 + 1 < m) {
            cvt[index * 2 + 1].sessionId = sessionId[index];
            cvt[index * 2 + 1].hasSession = "true";
        }
    }
}