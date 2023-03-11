class AlltrackSessionSuccess {
  final String? message;
  final String? timestamp;
  final String? adid;
  final String? jsonResponse;

  AlltrackSessionSuccess({
    required this.message,
    required this.timestamp,
    required this.adid,
    required this.jsonResponse,
  });

  factory AlltrackSessionSuccess.fromMap(dynamic map) {
    try {
      return AlltrackSessionSuccess(
        message: map['message'],
        timestamp: map['timestamp'],
        adid: map['adid'],
        jsonResponse: map['jsonResponse'],
      );
    } catch (e) {
      throw Exception(
          '[AlltrackFlutter]: Failed to create AlltrackSessionSuccess object from given map object. Details: ' +
              e.toString());
    }
  }
}
