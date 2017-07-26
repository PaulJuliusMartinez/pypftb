window.onload = () => {
  let pledgeButton = document.getElementById("pledge-button");
  pledgeButton.onclick = handlePledgeSubmit;
};

let handlePledgeSubmit = e => {
  // Don't let form submit.
  e.preventDefault();

  let pledgeForm = document.getElementById("pledge-form");
  let formData = new FormData(pledgeForm);

  let urlEncodedData = "";

  for (let key of formData.keys()) {
    if (urlEncodedData.length !== 0) urlEncodedData += '&';
    urlEncodedData += key;
    urlEncodedData += '=';
    urlEncodedData += encodeURIComponent(formData.get(key)).replace(/%20/g, '+');
  }

  let req = new XMLHttpRequest();
  let url = "/api/pledge";

  req.open("POST", url, true);

  req.onreadystatechange = () => {
    if (req.readyState === 4 && req.status === 400) {
      let pledgeError = document.getElementById("pledge-error");
      pledgeError.innerHTML = JSON.parse(req.responseText).message;
    }
  };

  req.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
  req.send(urlEncodedData);
};
