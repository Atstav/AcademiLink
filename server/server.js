require("dotenv").config();

const http = require("http");
const { Server } = require("socket.io");

const app = require("./app");
const connectDB = require("./config/db");

const PORT = process.env.PORT || 3000;

const startServer = async () => {
    await connectDB();

    const server = http.createServer(app);

    const io = new Server(server, {
        cors: {
            origin: "*",
            methods: ["GET", "POST"]
        }
    });

    io.on("connection", (socket) => {
        console.log(`Socket connected: ${socket.id}`);

        // Joins a private room using the user's ID
        socket.on("join_user", (userId) => {
            socket.join(userId);
            console.log(`User joined socket room: ${userId}`);
        });

        // Sends a private message to another user
        socket.on("send_message", (data) => {
            const message = {
                ...data,
                createdAt: new Date().toISOString()
            };

            io.to(data.receiverId).emit("receive_message", message);
        });

        socket.on("disconnect", () => {
            console.log(`Socket disconnected: ${socket.id}`);
        });
    });

    server.listen(PORT, () => {
        console.log(`AcademiLink server running on port ${PORT}`);
        console.log("Socket.io ready");
    });
};

startServer();