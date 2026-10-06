const { useEffect, useRef } = React;

function MediaDemo() {
  const canvasRef = useRef(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    const context = canvas.getContext("2d");

    context.clearRect(0, 0, canvas.width, canvas.height);

    context.font = "24px Arial";
    context.fillText("AcademiLink", 30, 50);

    context.font = "16px Arial";
    context.fillText("Student Social Network", 30, 85);

    context.strokeRect(20, 20, 300, 100);
  }, []);

  return React.createElement(
    "div",
    { className: "react-demo" },

    React.createElement(
      "div",
      null,

      React.createElement("h3", null, "Video"),

      React.createElement("video", {
        controls: true,
        width: 400,
        src: "/media/demo.mp4",
      }),
    ),

    React.createElement(
      "div",
      null,

      React.createElement("h3", null, "Canvas"),

      React.createElement("canvas", {
        ref: canvasRef,
        width: 350,
        height: 150,
      }),
    ),
  );
}

const root = ReactDOM.createRoot(
  document.getElementById("reactRoot")
);

root.render(
  React.createElement(MediaDemo)
);