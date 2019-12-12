import { expect } from 'chai';
import { variantEquals } from '@/lib/utils';

const SKELETON = {
  model: {
    all: true,
    brand: null,
    model: null,
    project: null
  },
  vehicle: [],
  equipment: [],
  exteriorColours: [],
  interiorColours: [],
  trimColours: [],
  cancelled: false
};

// mixins value with skeleton
function e (value) {
  const copy = Object.assign({}, SKELETON);

  for (let key of Object.keys(value)) {
    if (key === 'model') {
      copy.model = Object.assign({}, SKELETON.model);
      for (let mKey of Object.keys(value.model)) {
        if (mKey === 'all' || mKey === 'brand') {
          copy.model[mKey] = value.model[mKey];
        } else {
          copy.model[mKey] = { key: value.model[mKey], value: value.model[mKey] };
        }
      }
    } else if (Array.isArray(value[key])) {
      copy[key] = value[key].map(v => ({ key: v, value: v }));
    } else {
      copy[key] = value[key];
    }
  }

  return copy;
}

describe('utils', () => {
  describe('#variantEquals', () => {
    it('handles null values', () => {
      expect(variantEquals(null, null)).to.be.true;
      expect(variantEquals({}, null)).to.be.false;
      expect(variantEquals(null, {})).to.be.false;
    });

    it('compares cancelled status', () => {
      expect(variantEquals(
        e({ cancelled: false }),
        e({ cancelled: null })
      ), 'both not cancelled').to.be.true;

      expect(variantEquals(
        e({ cancelled: true }),
        e({ cancelled: true })
      ), 'both are cancelled').to.be.true;

      expect(variantEquals(
        e({ cancelled: true }),
        e({ cancelled: false })
      ), 'one is cancelled').to.be.false;

      expect(variantEquals(
        e({ cancelled: false }),
        e({ cancelled: true })
      ), 'the other is cancelled').to.be.false;
    });

    it('compares selected model', () => {
      expect(variantEquals(
        e({ model: { all: true } }),
        e({ model: { all: true } })
      ), 'both all models').to.be.true;

      expect(variantEquals(
        e({ model: { all: false } }),
        e({ model: { all: true } })
      ), 'not both all models').to.be.false;

      expect(variantEquals(
        e({ model: { all: false, brand: 'a', model: 'b', project: 'c' } }),
        e({ model: { all: false, brand: 'a', model: 'b', project: 'c' } })
      ), 'same project').to.be.true;

      expect(variantEquals(
        e({ model: { all: false, brand: 'a', model: 'b', project: 'c' } }),
        e({ model: { all: false, brand: 'd', model: 'b', project: 'c' } })
      ), 'different brand').to.be.false;

      expect(variantEquals(
        e({ model: { all: false, brand: 'a', model: 'b', project: 'c' } }),
        e({ model: { all: false, brand: 'a', model: 'd', project: 'c' } })
      ), 'different vehicle').to.be.false;

      expect(variantEquals(
        e({ model: { all: false, brand: 'a', model: 'b', project: 'c' } }),
        e({ model: { all: false, brand: 'a', model: 'b', project: 'd' } })
      ), 'different project').to.be.false;
    });

    const properties = [ 'vehicle', 'equipment', 'exteriorColours', 'interiorColours', 'trimColours' ];

    for (let property of properties) {
      it(`compares ${property} options`, () => {
        let v1;
        let v2;

        v1 = {};
        v2 = {};
        v1[property] = [ 'a', 'b', 'c' ];
        v2[property] = [ 'a', 'b', 'c' ];
        expect(variantEquals(e(v1), e(v2)), `same ${property} options`).to.be.true;

        v1 = {};
        v2 = {};
        v1[property] = [ 'a', 'b', 'c' ];
        v2[property] = [ 'c', 'a', 'b' ];
        expect(variantEquals(e(v1), e(v2)), `same ${property} options (different order)`).to.be.true;

        v1 = {};
        v2 = {};
        v1[property] = [ 'a', 'b', 'c' ];
        v2[property] = [ 'a', 'b' ];
        expect(variantEquals(e(v1), e(v2)), `e1 has more ${property} options than e2`).to.be.false;

        v1 = {};
        v2 = {};
        v1[property] = [ 'a', 'b' ];
        v2[property] = [ 'a', 'b', 'c' ];
        expect(variantEquals(e(v1), e(v2)), `e2 has more ${property} options than e1`).to.be.false;
      });
    }
  });
});
