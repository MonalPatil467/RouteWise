function hideAllPages(){

    document.getElementById("homePage").classList.add("hidden");

    document.getElementById("userLoginPage").classList.add("hidden");

    document.getElementById("driverLoginPage").classList.add("hidden");

    document.getElementById("rideSearchPage").classList.add("hidden");

    document.getElementById("driversPage").classList.add("hidden");

    document.getElementById("driverDashboard").classList.add("hidden");

    document.getElementById("successPage").classList.add("hidden");

}

function showUserLogin(){

    hideAllPages();

    document.getElementById("userLoginPage")
    .classList.remove("hidden");

}

function showDriverLogin(){

    hideAllPages();

    document.getElementById("driverLoginPage")
    .classList.remove("hidden");

}

function showRideSearch(){

    hideAllPages();

    document.getElementById("rideSearchPage")
    .classList.remove("hidden");

}

function showDrivers(){

    hideAllPages();

    document.getElementById("driversPage")
    .classList.remove("hidden");

}

function bookRide(driver,amount){

    alert(
        "Ride booked successfully with "
        + driver +
        " for ₹" +
        amount
    );

}

function showDriverDashboard(){

    hideAllPages();

    document.getElementById("driverDashboard")
    .classList.remove("hidden");

}

function acceptRide(){

    hideAllPages();

    document.getElementById("successPage")
    .classList.remove("hidden");

}