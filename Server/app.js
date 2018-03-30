const express = require('express');
const app = express();
const server = require('http').Server(app);
const io = require('socket.io')(server);
const OpenTok = require('opentok');

const API_KEY = '45961352';
const API_SECRET = 'c9c70eda35502ae4ea2597145991abbb00aba1e6';

// get initial session id 
var opentok = new OpenTok(API_KEY, API_SECRET);

var sessionId = [];
var numberOfSession = 0;
var m = 0,
    n = 0;
var member = [];
var cvt = [];
var infoRoom = [];

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
        console.log('Removed id : ' + fbId);
    }
}

Array.prototype.removeSocket = function(socket) {
    var position = -1;
    for (var i = 0; i < this.length; i++) {
        if (this[i].socket.id == socket.id) {
            position = i;
            break;
        }
    }
    if (position != -1) {
        this.splice(position, 1);
        console.log('Removed socket : ' + socket.id);
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
        console.log(data);

        //==== storage data from client

        if (data.role != "Member") {
            if (!member.contains(data)) {
                member.push({ id: data.fbId, name: data.name, sesionId: '', token: '', socket: socket });
                console.log("Total Member: " + member.length);
                if (cvt.length / 2 < sessionId.length) {
                    // createNewSession();
                }
            } else {
                console.log("This user is existed");
                member.remove(data.fbId);
                member.push({ id: data.fbId, name: data.name, sesionId: '', token: '', socket: socket });
                console.log("Updated this user");
            }
        } else if (!cvt.contains(data)) {

            cvt.push({ id: data.fbId, name: data.name, sesionId: '', token: '', socket: socket });
            console.log("Total ctv: " + cvt.length);
            // if (cvt.length % 2 == 0) {
            //     if (cvt.length / 2 > sessionId.length) {
            //         createNewSession();
            //     }
            // }
        } else {
            console.log("This user is existed");
            cvt.remove(data.fbId);
            cvt.push({ id: data.fbId, name: data.name, sesionId: '', token: '', socket: socket });
            console.log("Updated this user");
        }

        socket.on('cancel-attendant', function(fbId) {
            member.remove(fbId);
        });


        socket.on('get-info-rooms', function(data) {
            console.log(data);
            console.log(infoRoom);

            socket.emit('return-info', infoRoom);
        });

    });

    socket.on('disconnect', function() {
        console.log('User disconnected');
        member.removeSocket(socket);
        cvt.removeSocket(socket);
    });

    socket.on('start-call', function(data) {
        console.log('You clicked on the button!')
        start();
    });
})

function sendToMember(index) {
    if (member[index] != null) {
        console.log("Session ID sending to member " + index + ":\n" + member[index].sessionId);
        var id2 = "";
        var id3 = "";

        if (index * 2 < m) id2 = cvt[index * 2].id;
        if (index * 2 + 1 < m) id3 = cvt[index * 2 + 1].id;

        member[index].socket.emit('return-session-id', {
            indexSession: index,
            sessionId: member[index].sessionId,
            token: member[index].token,
            id2: id2,
            id3: id3
        });


    }
}

function sendToCtv(index) {
    if (cvt[index] != null) {
        console.log("Session ID sending to cvt " + index + ":\n" + cvt[index].sessionId);
        var id2 = "";
        var id3 = "";

        var mIndex = Math.floor(index / 2);
        if (mIndex < n) id2 = member[mIndex].id;
        if (index % 2 == 0) {
            if (index + 1 < m) id3 = cvt[index + 1].id;
        } else {
            id3 = cvt[index - 1].id
        }

        cvt[index].socket.emit('return-session-id', {
            indexSession: mIndex,
            sessionId: cvt[index].sessionId,
            token: opentok.generateToken(cvt[index].sessionId),
            id2: id2,
            id3: id3
        });

    }
}

function createNewSession() {
    opentok.createSession({ mediaMode: "routed" }, function(error, session) {
        if (error) {
            console.log("Error creating session:\n", error)
        } else {
            sessionId.push(session.sessionId);
            console.log("Session ID: " + sessionId[sessionId.length - 1]);
            if (sessionId.length < 50) {
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
    n = member.length;
    m = cvt.length;
    console.log("Total member: " + n);
    console.log("Total ctv:" + m);
    numberOfSession = Math.max(n, Math.trunc((m + 1) / 2))
    console.log("max: " + numberOfSession);
    infoRoom.splice(0, infoRoom.length);
    for (var index = 0; index < numberOfSession; index++) {
        var userId1 = "";
        var userId2 = "";
        var userId3 = "";
        if (index < n) {
            member[index].sessionId = sessionId[index];
            member[index].token = opentok.generateToken(member[index].sessionId);
            sendToMember(index);
            userId1 = member[index].id;
        }
        if (index * 2 < m) {
            console.log('index:' + (index * 2));
            cvt[index * 2].sessionId = sessionId[index];
            cvt[index * 2].token = opentok.generateToken(cvt[index * 2].sessionId);
            sendToCtv(index * 2);
            userId2 = cvt[index * 2].id;
        }
        if (index * 2 + 1 < m) {
            cvt[index * 2 + 1].sessionId = sessionId[index];
            cvt[index * 2 + 1].token = opentok.generateToken(cvt[index * 2].sessionId);
            sendToCtv(index * 2 + 1);
            userId3 = cvt[index * 2 + 1].id;
        }
        infoRoom.push({
            roomNumber: index,
            userId1: userId1,
            userId2: userId2,
            userId3: userId3
        });

    }
    member.splice(0, member.length);
    cvt.splice(0, cvt.length);
    sessionId.splice(0, numberOfSession);
    createNewSession();
}