class AlltrackEventFailure {
  final String? message;
  final String? timestamp;
  final String? adid;
  final String? eventToken;
  final String? callbackId;
  final String? jsonResponse;
  final bool? willRetry;

  AlltrackEventFailure({
    required this.message,
    required this.timestamp,
    required this.adid,
    required this.eventToken,
    required this.callbackId,
    required this.jsonResponse,
    required this.willRetry,
  });

  factory AlltrackEventFailure.fromMap(dynamic map) {
    try {
      return AlltrackEventFailure(
        message: map['message'],
        timestamp: map['timestamp'],
        adid: map['adid'],
        eventToken: map['eventToken'],
        callbackId: map['callbackId'],
        jsonResponse: map['jsonResponse'],
        willRetry: map['willRetry']?.toString().toLowerCase() == 'true',
      );
    } catch (e) {
      throw Exception(
          '[AlltrackFlutter]: Failed to create AlltrackEventFailure object from given map object. Details: ' +
              e.toString());
    }
  }
}
