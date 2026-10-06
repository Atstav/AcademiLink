const mongoose = require("mongoose");

const postSchema = new mongoose.Schema(
    {
        content: {
            type: String,
            required: true,
            trim: true,
            maxlength: 1000
        },
        category: {
            type: String,
            required: true,
            enum: [
                "Question",
                "Study Material",
                "Homework",
                "Exam",
                "General"
            ]
        },
        authorId: {
            type: mongoose.Schema.Types.ObjectId,
            ref: "User",
            required: true
        },
        groupId: {
            type: mongoose.Schema.Types.ObjectId,
            ref: "StudyGroup",
            required: true
        }
    },
    { timestamps: true }
);

module.exports = mongoose.model("Post", postSchema);