import React, { useState, useRef, useEffect } from "react";
import { TbRobot } from "react-icons/tb";
import { FaUser } from "react-icons/fa";
import { RiRobot3Fill } from "react-icons/ri";
import axios from "axios";

function App() {
  const [input, setInput] = useState("");
  const [messages, setMessages] = useState([
    
  ]);

  const chatEndRef = useRef(null);

  const scrollToBottom = () => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  
  useEffect(() => {
    scrollToBottom();
  }, [messages]);

  // New reusable function for sending a question to API
  const askQuestion = async (question, showUserMessage = true) => {
    if (!question.trim()) return;
  
    if (showUserMessage) {
      const userMessage = { from: "Manohar", text: question };
      setMessages((prev) => [...prev, userMessage]);
    }
  
    try {
      const response = await axios.get("http://localhost:8080/user/chat", {
        params: { question },
        headers: { Accept: "application/json" },
      });
  
      const samReply = { from: "Sam", text: response.data };
      setMessages((prev) => [...prev, samReply]);
    } catch (error) {
      setMessages((prev) => [
        ...prev,
        { from: "Sam", text: "Oops! Something went wrong." },
      ]);
    }
  };
  

  const hasAskedOnce = useRef(false);

  useEffect(() => {
    if (!hasAskedOnce.current) {
      askQuestion("who are you?", false);
      hasAskedOnce.current = true;
    }
  }, []);
  
  const sendMessage = () => {
    askQuestion(input);
    setInput("");
  };

  return (
    <div>
      <div style={styles.container}>
        <h2 style={styles.header}>
        <RiRobot3Fill
                  style={{ fontSize: 40, color: "red", marginRight: 10 }}
                /> TEST AI Chat System
        </h2>

        <div style={styles.chatBox}>
          {messages.map((msg, idx) => (
            <div
              key={idx}
              style={{
                display: "flex",
                justifyContent: msg.from === "Manohar" ? "flex-end" : "flex-start",
                alignItems: "flex-end",
                marginBottom: 10,
              }}
            >
              {msg.from === "Sam" && (
                <TbRobot
                  style={{ fontSize: 35, color: "#0b03fc", marginRight: 10 }}
                />
              )}
              <div
                style={{
                  ...styles.message,
                  backgroundColor: msg.from === "Manohar" ? "#dcf8c6" : "#e1e1e1",
                  borderRadius:
                    msg.from === "Manohar" ? "15px 15px 0 15px" : "15px 15px 15px 0",
                  textAlign: "left",
                }}
              >
                <strong>{msg.from}:</strong> {msg.text}
              </div>
              {msg.from === "Manohar" && (
                <FaUser
                  style={{ fontSize: 30, color: "#4caf50", marginLeft: 10 }}
                />
              )}
            </div>
          ))}
          <div ref={chatEndRef} />
        </div>

        <div style={styles.inputArea}>
          <input
            style={styles.input}
            type="text"
            placeholder="Type a message..."
            value={input}
            onChange={(e) => setInput(e.target.value)}
            onKeyDown={(e) => e.key === "Enter" && sendMessage()}
          />
          <button style={styles.button} onClick={sendMessage}>
            Send
          </button>
        </div>
      </div>
    </div>
  );
}

const styles = {
  container: {
    maxWidth: 600,
    margin: "30px auto",
    padding: 20,
    fontFamily: "Arial, sans-serif",
    backgroundColor: "#f5f5f5",
    borderRadius: 10,
    boxShadow: "0 2px 10px rgba(0,0,0,0.1)",
  },
  header: {
    textAlign: "center",
    marginBottom: 20,
  },
  chatBox: {
    backgroundColor: "#fff",
    borderRadius: 10,
    padding: 15,
    height: "500px",
    overflowY: "auto",
    border: "1px solid #ddd",
  },
  message: {
    padding: "10px 15px",
    maxWidth: "70%",
    wordWrap: "break-word",
    boxShadow: "0 1px 3px rgba(0,0,0,0.1)",
  },
  avatar: {
    width: 30,
    height: 30,
    borderRadius: "50%",
    margin: "0 10px",
  },
  inputArea: {
    marginTop: 15,
    display: "flex",
    gap: 10,
  },
  input: {
    flexGrow: 1,
    padding: 10,
    borderRadius: 5,
    border: "1px solid #ccc",
    fontSize: 16,
  },
  button: {
    padding: "10px 15px",
    backgroundColor: "#4caf50",
    color: "#fff",
    border: "none",
    borderRadius: 5,
    cursor: "pointer",
  },
};

export default App;
