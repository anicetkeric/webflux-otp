class PageResponse {
  int totalPages;
  int totalItems;
  int currentPage;
  int firstPage;
  int lastPage;
  bool first;
  bool last;
  int itemsPerPage;
  int pageSize;
  Sort sort;
  List<dynamic> items;

  PageResponse(
      {this.totalPages = 1,
        this.totalItems = 1,
        this.currentPage = 1,
        this.firstPage = 1,
        this.lastPage = 1,
        this.first = true,
        this.last = true,
        this.itemsPerPage = 1,
        this.pageSize = 20,
        required this.sort,
        required this.items});

  factory PageResponse.fromJson(Map<String, dynamic> json) {
    return PageResponse(
        totalPages : json['totalPages'],
        totalItems : json['totalItems'],
        currentPage : json['currentPage'],
        firstPage : json['firstPage'],
        lastPage : json['lastPage'],
        first : json['first'],
        last : json['last'],
        itemsPerPage : json['itemsPerPage'],
        pageSize : json['pageSize'],
        sort :Sort.fromJson(json['sort']),
        items: json['items']
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['totalPages'] = totalPages;
    data['totalItems'] = totalItems;
    data['currentPage'] = currentPage;
    data['firstPage'] = firstPage;
    data['lastPage'] = lastPage;
    data['first'] = first;
    data['last'] = last;
    data['itemsPerPage'] = itemsPerPage;
    data['pageSize'] = pageSize;
    data['sort'] = sort.toJson();
    data['items'] = items.map((v) => v.toJson()).toList();

    return data;
  }
}

class Sort {
  bool sorted;
  bool unsorted;
  bool empty;

  Sort({this.sorted = false, this.unsorted = true, this.empty = true});

  factory Sort.fromJson(Map<String, dynamic> json) {
    return Sort(
        sorted : json['sorted'],
        unsorted : json['unsorted'],
        empty : json['empty']
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['sorted'] = sorted;
    data['unsorted'] = unsorted;
    data['empty'] = empty;
    return data;
  }
}