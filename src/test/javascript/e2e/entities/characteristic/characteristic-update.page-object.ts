import { element, by, ElementFinder } from 'protractor';

export default class CharacteristicUpdatePage {
  pageTitle: ElementFinder = element(by.id('adminJhipsterApp.characteristic.home.createOrEditLabel'));
  saveButton: ElementFinder = element(by.id('save-entity'));
  cancelButton: ElementFinder = element(by.id('cancel-save'));
  characteristicIdInput: ElementFinder = element(by.css('input#characteristic-characteristicId'));
  serviceIdInput: ElementFinder = element(by.css('input#characteristic-serviceId'));
  descripcionInput: ElementFinder = element(by.css('input#characteristic-descripcion'));
  serviceTypeSelect: ElementFinder = element(by.css('select#characteristic-serviceType'));

  getPageTitle() {
    return this.pageTitle;
  }

  async setCharacteristicIdInput(characteristicId) {
    await this.characteristicIdInput.sendKeys(characteristicId);
  }

  async getCharacteristicIdInput() {
    return this.characteristicIdInput.getAttribute('value');
  }

  async setServiceIdInput(serviceId) {
    await this.serviceIdInput.sendKeys(serviceId);
  }

  async getServiceIdInput() {
    return this.serviceIdInput.getAttribute('value');
  }

  async setDescripcionInput(descripcion) {
    await this.descripcionInput.sendKeys(descripcion);
  }

  async getDescripcionInput() {
    return this.descripcionInput.getAttribute('value');
  }

  async serviceTypeSelectLastOption() {
    await this.serviceTypeSelect
      .all(by.tagName('option'))
      .last()
      .click();
  }

  async serviceTypeSelectOption(option) {
    await this.serviceTypeSelect.sendKeys(option);
  }

  getServiceTypeSelect() {
    return this.serviceTypeSelect;
  }

  async getServiceTypeSelectedOption() {
    return this.serviceTypeSelect.element(by.css('option:checked')).getText();
  }

  async save() {
    await this.saveButton.click();
  }

  async cancel() {
    await this.cancelButton.click();
  }

  getSaveButton() {
    return this.saveButton;
  }
}
