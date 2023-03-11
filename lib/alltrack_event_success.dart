class AlltrackEventSuccess {
  final String? message;
  final String? timestamp;
  final String? adid;
  final String? eventToken;
  final String? callbackId;
  final String? jsonResponse;

  AlltrackEventSuccess({
    required this.message,
    required this.timestamp,
    required this.adid,
    required this.eventToken,
    required this.callbackId,
    required this.jsonResponse,
  });

  factory AlltrackEventSuccess.fromMap(dynamic map) {
    try {
      return AlltrackEventSuccess(
        message: map['message'],
        timestamp: map['timestamp'],
        adid: map['adid'],
        eventToken: map['eventToken'],
        callbackId: map['callbackId'],
        jsonResponse: map['jsonResponse'],
      );
    } catch (e) {
      throw Exception(
          '[AlltrackFlutter]: Failed to create AlltrackEventSuccess object from given map object. Details: ' +
              e.toString());
    }
  }
}
