
function authRequest(url, options = {}) {

    const token =
        localStorage.getItem("token");

    const headers = {
        ...(options.headers || {}),
        "Content-Type": "application/json"
    };

    if (token) {
        headers["Authorization"] =
            "Bearer " + token;
    }

    return fetch(url, {
        ...options,
        headers: headers
    });
}
function getDeviceId(){
    let id = localStorage.getItem("device_id");

    if(!id){
        id = crypto.randomUUID();
        localStorage.setItem(
            "device_id",
            id
        );
    }
    return id;
}
function getOs(){
    let ua = navigator.userAgent;

    if(ua.includes("Windows NT 10.0")){
        return "Windows 10/11";
    }

    if(ua.includes("Mac OS X")){
        return "macOS";
    }

    if(ua.includes("Android")){
        return "Android";
    }

    if(
        ua.includes("iPhone")
        ||
        ua.includes("iPad")
    ){
        return "iOS";
    }
    return "Unknown";
}