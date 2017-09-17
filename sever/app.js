const express = require('express');
const app = express();
const server = require('http').Server(app);
const io = require('socket.io')(server);
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
        if (this[i].id == data.fbId) {
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

Array.prototype.remove = function(fbId) {
    var position = -1;
    for (var i = 0; i < this.length; i++) {
        if (this[i].id == fbId) {
            position = i;
            break;
        }
    }
    if (position != -1) {
        this.splice(position, 1);
        console.log('Removed id ' + fbId);
    }

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

app.use('/', express.static(__dirname));
app.get('/start', function(req, res) {
    res.sendFile(__dirname + '/start.html');
});

io.on('connection', function(socket) {

    socket.on('test', function(data) {
        console.log(data);
    });

    socket.on('get-session-id', function(data) {
        {
            //==== storage data from client

            if (data.role != "Member") {
                if (!member.contains(data)) {
                    console.log(data);
                    var mIndex = member.length;
                    member.push({ id: data.fbId, name: data.name, hasSession: "false", sesionId: "", socket: socket });
                    console.log("Member: " + member.length);
                    var ok = false;
                    var check = setInterval(function() {
                        if (member[mIndex] != null && member[mIndex].hasSession === "true") {
                            if (ok === true) {
                                return;
                            }
                            console.log("Session ID sending to member " + (mIndex + 1) + ":\n" + member[mIndex].sessionId);
                            var name2 = "";
                            var name3 = "";
                            var i;
                            for (i = 0; i < m; i++) {
                                if (cvt[i].sessionId == member[mIndex].sessionId) {
                                    name2 = cvt[i].name;
                                    break;
                                }
                            }
                            for (var j = i + 1; j < m; j++) {
                                if (cvt[j].sessionId == member[mIndex].sessionId) {
                                    name3 = cvt[j].name;
                                    break;
                                }
                            }

                            socket.emit('return-session-id', {
                                indexSession: mIndex,
                                sessionId: member[mIndex].sessionId,
                                token: opentok.generateToken(member[mIndex].sessionId),
                                name1: data.name,
                                name2: name2,
                                name3: name3
                            });
                            ok = true;
                        }
                    }, 1000);
                } else {
                    console.log("This user is existed");
                    member.remove(data.fbId);
                    var mIndex = member.length;
                    member.push({ id: data.fbId, name: data.name, hasSession: "false", sesionId: "", socket: socket });
                    console.log("Member: " + member.length);
                    var ok = false;
                    var check = setInterval(function() {
                        if (member[mIndex] != null && member[mIndex].hasSession === "true") {
                            if (ok === true) {
                                return;
                            }
                            console.log("Session ID sending to member " + (mIndex + 1) + ":\n" + member[mIndex].sessionId);
                            var name2 = "";
                            var name3 = "";
                            var i;
                            for (i = 0; i < m; i++) {
                                if (cvt[i].sessionId == member[mIndex].sessionId) {
                                    name2 = cvt[i].name;
                                    break;
                                }
                            }
                            for (var j = i + 1; j < m; j++) {
                                if (cvt[j].sessionId == member[mIndex].sessionId) {
                                    name3 = cvt[j].name;
                                    break;
                                }
                            }

                            socket.emit('return-session-id', {
                                indexSession: mIndex,
                                sessionId: member[mIndex].sessionId,
                                token: opentok.generateToken(member[mIndex].sessionId),
                                name1: data.name,
                                name2: name2,
                                name3: name3
                            });
                            ok = true;
                        }
                    }, 1000);
                }
            } else if (!cvt.contains(data)) {
                console.log(data);
                var cIndex = cvt.length;
                cvt.push({ id: data.fbId, name: data.name, hasSession: "false", sesionId: "", socket: socket });
                console.log("CTV: " + cvt.length);
                var ok = false;
                var check = setInterval(function() {
                    if (cvt[cIndex] != null && cvt[cIndex].hasSession === "true") {
                        if (ok === true) {
                            return;
                        }
                        console.log("Session ID sending to cvt " + (cIndex + 1) + ":\n" + cvt[cIndex].sessionId);
                        var name2 = "";
                        var name3 = "";
                        for (var i = 0; i < n; i++) {
                            if (member[i].sessionId == cvt[cIndex].sessionId) {
                                name2 = member[i].name;
                                break;
                            }
                        }
                        for (var i = 0; i < m; i++) {
                            if (i != cIndex && cvt[i].sessionId == cvt[cIndex].sessionId) {
                                name3 = cvt[i].name;
                                break;
                            }
                        }
                        if (cIndex % 2 == 0) {
                            socket.emit('return-session-id', {
                                indexSession: cIndex / 2,
                                sessionId: cvt[cIndex].sessionId,
                                token: opentok.generateToken(cvt[cIndex].sessionId),
                                name1: data.name,
                                name2: name2,
                                name3: name3
                            });
                            console.log({
                                name1: data.name,
                                name2: name2,
                                name3: name3
                            });
                        } else {
                            socket.emit('return-session-id', {
                                indexSession: cIndex / 2,
                                sessionId: cvt[cIndex].sessionId,
                                token: opentok.generateToken(cvt[cIndex].sessionId),
                                name1: data.name,
                                name2: name2,
                                name3: name3
                            });
                            console.log({
                                name1: data.name,
                                name2: name2,
                                name3: name3
                            });
                        }
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
                cvt.remove(data.fbId);
                console.log("Updated");
                var cIndex = cvt.length;
                cvt.push({ id: data.fbId, name: data.name, hasSession: "false", sesionId: "", socket: socket });
                console.log("CTV: " + cvt.length);
                var ok = false;
                var check = setInterval(function() {
                    if (cvt[cIndex] != null && cvt[cIndex].hasSession === "true") {
                        if (ok === true) {
                            return;
                        }
                        console.log("Session ID sending to cvt " + (cIndex + 1) + ":\n" + cvt[cIndex].sessionId);
                        var name2 = "";
                        var name3 = "";
                        for (var i = 0; i < n; i++) {
                            if (member[i].sessionId == cvt[cIndex].sessionId) {
                                name2 = member[i].name;
                                break;
                            }
                        }
                        for (var i = 0; i < m; i++) {
                            if (i != cIndex && cvt[i].sessionId == cvt[cIndex].sessionId) {
                                name3 = cvt[i].name;
                                break;
                            }
                        }
                        if (cIndex % 2 == 0) {
                            socket.emit('return-session-id', {
                                indexSession: cIndex / 2,
                                sessionId: cvt[cIndex].sessionId,
                                token: opentok.generateToken(cvt[cIndex].sessionId),
                                name1: data.name,
                                name2: name2,
                                name3: name3
                            });
                            console.log({
                                name1: data.name,
                                name2: name2,
                                name3: name3
                            });
                        } else {
                            socket.emit('return-session-id', {
                                indexSession: cIndex / 2,
                                sessionId: cvt[cIndex].sessionId,
                                token: opentok.generateToken(cvt[cIndex].sessionId),
                                name1: data.name,
                                name2: name2,
                                name3: name3
                            });
                            console.log({
                                name1: data.name,
                                name2: name2,
                                name3: name3
                            });
                        }
                        ok = true;
                    }
                }, 1000);
                if (cvt.length % 2 == 0) {
                    if (cvt.length / 2 > sessionId.length) {
                        createNewSession();
                    }
                }
            }

            socket.on('cancel-attendant', function(fbId) {
                // console.log(fbId);
                member.remove(fbId);
                cvt.remove(fbId);
            });

            socket.on('get-info-rooms', function(data) {
                console.log(data);
                var data = [];
                for (var i = 0; i < sessionId.length; i++) {
                    var userId1 = "";
                    var userName1 = "";
                    for (var iMember = 0; iMember < member.length; iMember++) {
                        if (member[iMember].sessionId == sessionId[i]) {
                            userId1 = member[iMember].id;
                            userName1 = member[iMember].name;
                            break;
                        }
                    }
                    var userId2 = "";
                    var userName2 = "";
                    var iCvt;
                    for (iCvt = 0; iCvt < cvt.length; iCvt++) {
                        if (cvt[iCvt].sessionId == sessionId[i]) {
                            userId2 = cvt[iCvt].id;
                            userName2 = cvt[iCvt].name;
                            break;
                        }
                    }
                    var userId3 = "";
                    var userName3 = "";
                    for (iCvt = iCvt + 1; iCvt < cvt.length; iCvt++) {
                        if (cvt[iCvt].sessionId == sessionId[i]) {
                            userId3 = cvt[iCvt].id;
                            userName3 = cvt[iCvt].name;
                            break;
                        }
                    }
                    var aRoom = { roomNumber: i, userId1, userId2, userId3, userName1, userName2, userName3 };
                    data.push(aRoom);
                    console.log(data);
                }
                socket.emit('return-info', data);
            });

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