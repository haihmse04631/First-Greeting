
module.exports = (io) => {

    io.on('connection', function (socket) {

        console.log('A user connected ');
        
        socket.on('client-send', function(data){
            console.log(data);
            io.emit('server-send', {noidung: 'ok'});            
        })
    });
};