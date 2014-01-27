'use strict';

describe('Service: LifeSym', function () {

  // load the service's module
  beforeEach(module('webApp'));

  // instantiate service
  var LifeSym;
  beforeEach(inject(function (_LifeSym_) {
    LifeSym = _LifeSym_;
  }));

  it('should do something', function () {
    expect(!!LifeSym).toBe(true);
  });

});
