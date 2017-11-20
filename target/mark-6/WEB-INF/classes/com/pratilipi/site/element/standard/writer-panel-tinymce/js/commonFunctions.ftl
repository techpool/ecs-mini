function jsonParseTryCatch(data) {
  var parsed_data;
  try {
    parsed_data = JSON.parse(data);
  }
  catch(err) {
    parsed_data = data;
  }
  return parsed_data;
}
