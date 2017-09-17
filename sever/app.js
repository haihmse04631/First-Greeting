const express = require('express');
const app = express();
const server = require('http').Server(app);
const io = require('socket.io')(server);
const OpenTok = require('opentok');

const API_KEY = '45961352';
const API_SECRET = 'c9c70eda35502ae4ea2597145991abbb00aba1e6';

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

// server.listen(process.env.PORT, () => {
//     console.log(`Server is running at localhost: ` + process.env.PORT);
// });

server.listen(process.env.PORT || 3000, function() {
    console.log("Express server listening on port %d", this.address().port);
});

// server.listen(3000, () => {
//     console.log(`Server is running at localhost:3000`);
// });


app.use('/', express.static(__dirname));

app.get('/', function(req, res) {
    res.sendFile(__dirname + '/index.html');
});

io.on('connection', function(socket) {

    socket.on('get-session-id', function(data) {
        //==== storage data from client
        if (data.role != "Member") {
            if (!member.contains(data)) {
                console.log(data);
                member.push({ id: data.fbId, name: data.name, sesionId: "", socket: socket });
                console.log("Total Member: " + member.length);
                if (cvt.length / 2 < sessionId.length) {
                    // createNewSession();
                }
            } else {
                console.log("This user is existed");
            }
        } else if (!cvt.contains(data)) {
            console.log(data);
            cvt.push({ id: data.fbId, name: data.name, sesionId: "", socket: socket });
            console.log("Total ctv: " + cvt.length);
            if (cvt.length % 2 == 0) {
                if (cvt.length / 2 > sessionId.length) {
                    // createNewSession();
                }
            }
        } else {
            console.log("This user is existed");
        }

        socket.on('cancel-attendant', function(fbId) {
            // console.log(fbId);
            member.remove(fbId);
            cvt.remove(fbId);
        });

        // socket.on('get-info-rooms', function(data) {
        //     // console.log(data);
        //     var data = [];
        //     for (var i = 0; i < sessionId.length; i++) {
        //         var userId1 = "";
        //         var userName1 = "";
        //         for (var iMember = 0; iMember < member.length; iMember++) {
        //             if (member[iMember].sessionId == sessionId[i]) {
        //                 userId1 = member[iMember].id;
        //                 userName1 = member[iMember].name;
        //                 break;
        //             }
        //         }
        //         var userId2 = "";
        //         var userName2 = "";
        //         var iCvt;
        //         for (iCvt = 0; iCvt < cvt.length; iCvt++) {
        //             if (cvt[iCvt].sessionId == sessionId[i]) {
        //                 userId2 = cvt[iCvt].id;
        //                 userName2 = cvt[iCvt].name;
        //                 break;
        //             }
        //         }
        //         var userId3 = "";
        //         var userName3 = "";
        //         for (iCvt = iCvt + 1; iCvt < cvt.length; iCvt++) {
        //             if (cvt[iCvt].sessionId == sessionId[i]) {
        //                 userId3 = cvt[iCvt].id;
        //                 userName3 = cvt[iCvt].name;
        //                 break;
        //             }
        //         }
        //         var aRoom = { roomNumber: i, userId1, userId2, userId3, userName1, userName2, userName3 };
        //         data.push(aRoom);
        //         // console.log(data);
        //     }
        //     socket.emit('return-info', data);
    });

    socket.on('disconnect', function() {
        console.log('User ' + socket + " disconnected");
    });

    socket.on('start-call', function(data) {
        start();
    });
});

function sendToMember(index) {
    if (member[index] != null) {
        console.log("Session ID sending to member " + index + ":\n" + member[index].sessionId);
        var name1 = member[index].name;
        var name2 = "";
        var name3 = "";

        if (index * 2 < m) name2 = cvt[index * 2].name;
        if (index * 2 + 1 < m) name3 = cvt[index * 2 + 1].name;

        // member[index].socket.emit('resp', "test thu");

        // console.log('Dang tao token');
        var tk = opentok.generateToken(member[index].sessionId);

        member[index].socket.emit('return-session-id', member[index].sessionId);
        member[index].socket.emit('return-token-id', tk);
        member[index].socket.emit('return-name1', name1);
        member[index].socket.emit('return-name2', name2);
        member[index].socket.emit('return-name3', name3);
        member[index].socket.emit('return-room', index);



        // console.log('Token: ' + tk);

        // member[index].socket.emit('return-session-id', {
        //     indexSession: index,
        //     sessionId: member[index].sessionId,
        //     token: tk,
        //     name1: name1,
        //     name2: name2,
        //     name3: name3
        // });

    }
}

function sendToCtv(index) {
    if (cvt[index] != null) {
        console.log("Session ID sending to cvt " + index + ":\n" + cvt[index].sessionId);
        var name1 = cvt[index].name;
        var name2 = "";
        var name3 = "";

        var mIndex = Math.floor(index / 2);
        if (mIndex < n) name2 = member[mIndex].name;
        if (index % 2 == 0) {
            if (index + 1 < m) name3 = cvt[index + 1].name;
        } else {
            name3 = cvt[index - 1].name
        }

        var tk = opentok.generateToken(cvt[index].sessionId);

        cvt[index].socket.emit('return-session-id', cvt[index].sessionId);
        cvt[index].socket.emit('return-token-id', tk);
        cvt[index].socket.emit('return-name1', name1);
        cvt[index].socket.emit('return-name2', name2);
        cvt[index].socket.emit('return-name3', name3);
        cvt[index].socket.emit('return-room', index);

        // cvt[index].socket.emit('return-session-id', {
        //     indexSession: mIndex,
        //     sessionId: cvt[index].sessionId,
        //     token: opentok.generateToken(cvt[index].sessionId),
        //     name1: name1,
        //     name2: name2,
        //     name3: name3
        // });
        // cvt[index].socket.emit('resp', 'emit thu phat');
    }
}

function createNewSession() {
    opentok.createSession({ mediaMode: "routed" }, function(error, session) {
        if (error) {
            console.log("Error creating session:\n", error)
        } else {
            sessionId.push(session.sessionId);
            console.log("Session ID: " + sessionId[sessionId.length - 1]);
            if (sessionId.length < 70) {
                createNewSession();
            }
        }
    });



}

createNewSession();

// Start connecting users
function start() {
    console.log("Starting\nNumer of session: " + sessionId.length);
    scramble(member);
    scramble(cvt);
    scramble(sessionId);
    member[0].socket.emit('resp', 'emit thu phat');
    n = member.length;
    m = cvt.length;
    console.log("Total member: " + n);
    console.log("Total ctv:" + m);
    var numberOfSession = Math.max(n, Math.trunc(m / 2))
    console.log("max: " + numberOfSession);
    for (var index = 0; index < sessionId.length; index++) {
        if (index < n) {
            member[index].sessionId = sessionId[index];
            sendToMember(index);
        }
        if (index * 2 < m) {
            cvt[index * 2].sessionId = sessionId[index];
            sendToCtv(index * 2);
        }
        if (index * 2 + 1 < m) {
            cvt[index * 2 + 1].sessionId = sessionId[index];
            sendToCtv(index * 2 + 1);
        }
    }
}