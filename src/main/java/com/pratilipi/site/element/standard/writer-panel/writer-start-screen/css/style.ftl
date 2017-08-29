.horizontal-form-input {
	font-size: 18px;
    border: none;
    box-shadow: none !important;
    border-bottom: 1px solid black;
    border-radius: 0px;
    /*color: #a5a0a0;*/
}
.horizontal-form-input:hover, .horizontal-form-input:focus {
    box-shadow: none;
}
form .form-group {
    margin-bottom: 30px;
    margin-top: 15px;
}
.pratilipi-red-button {
    background: #FFFFFF;
    border: 1px solid #d0021b;
    box-shadow: 0px 1px 1px 0px rgba(0, 0, 0, 0.50);
    border-radius: 5px;
    font-size: 13px;
    color: #d0021b;
    letter-spacing: 0.3px;
    line-height: 16px;
    text-shadow: 0px 1px 2px #FFFFFF;
    display: inline-block;
    padding: 10px;
    margin-right: 10px;
    magin-top: 5px;
    outline: none;
} 
.pratilipi-red-background-button {
    background: #d0021b;
    border: 1px solid #d0021b;
    box-shadow: 0px 1px 1px 0px rgba(0, 0, 0, 0.50);
    border-radius: 5px;
    font-size: 13px;
    color: #ffffff;
    letter-spacing: 0.3px;
    line-height: 16px;
    display: inline-block;
    padding: 10px;
    margin-right: 10px;
    magin-top: 5px;
    outline: none;
} 
.go-button {
    margin-top: 20px;
    padding-left: 20px;
    padding-right: 20px;
    font-size: 20px;
    text-shadow: none;
}    
.pratilipi-panel-heading {
  margin-bottom: 20px; 
  margin-top: 40px;
} 
.grey-color {
  color:#a5a0a0;
}
.pratilipi-red {
  color:#d0021b;
}    

.has-error #copyright_checkbox {
	outline: #d0021b 1px solid;
    outline-offset: -2px;
} 

.spinner {
  position: absolute;
   top: 0;
   left: 0;
   width: 100%;
   height: 100%;
   background: rgba(255,255,255,0.8);
}

.spinner:before, .spinner:after {
  position: absolute;
  top: 50%;
  left: 50%;
  margin-left: -50px;
  margin-top: -50px;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background-color: #333;
  opacity: 0.6;
  content: '';
  
  -webkit-animation: sk-bounce 2.0s infinite ease-in-out;
  animation: sk-bounce 2.0s infinite ease-in-out;
}

.spinner:after {
  -webkit-animation-delay: -1.0s;
  animation-delay: -1.0s;
}

@-webkit-keyframes sk-bounce {
  0%, 100% { -webkit-transform: scale(0.0) }
  50% { -webkit-transform: scale(1.0) }
}

@keyframes sk-bounce {
  0%, 100% { 
    transform: scale(0.0);
    -webkit-transform: scale(0.0);
  } 50% { 
    transform: scale(1.0);
    -webkit-transform: scale(1.0);
  }
}